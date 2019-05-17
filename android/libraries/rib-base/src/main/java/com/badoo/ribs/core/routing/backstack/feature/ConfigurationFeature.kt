package com.badoo.ribs.core.routing.backstack.feature

import android.os.Parcelable
import com.badoo.mvicore.element.Actor
import com.badoo.mvicore.element.Bootstrapper
import com.badoo.mvicore.element.Reducer
import com.badoo.mvicore.element.TimeCapsule
import com.badoo.mvicore.feature.ActorReducerFeature
import com.badoo.ribs.core.Node
import com.badoo.ribs.core.routing.action.RoutingAction
import com.badoo.ribs.core.routing.backstack.ConfigurationCommand
import com.badoo.ribs.core.routing.backstack.ConfigurationCommand.MultiConfigurationCommand
import com.badoo.ribs.core.routing.backstack.ConfigurationCommand.MultiConfigurationCommand.SaveInstanceState
import com.badoo.ribs.core.routing.backstack.ConfigurationCommand.MultiConfigurationCommand.Sleep
import com.badoo.ribs.core.routing.backstack.ConfigurationCommand.MultiConfigurationCommand.WakeUp
import com.badoo.ribs.core.routing.backstack.ConfigurationCommand.SingleConfigurationCommand
import com.badoo.ribs.core.routing.backstack.ConfigurationCommand.SingleConfigurationCommand.Activate
import com.badoo.ribs.core.routing.backstack.ConfigurationCommand.SingleConfigurationCommand.Add
import com.badoo.ribs.core.routing.backstack.ConfigurationCommand.SingleConfigurationCommand.Deactivate
import com.badoo.ribs.core.routing.backstack.ConfigurationCommand.SingleConfigurationCommand.Remove
import com.badoo.ribs.core.routing.backstack.ConfigurationContext
import com.badoo.ribs.core.routing.backstack.ConfigurationContext.ActivationState.ACTIVE
import com.badoo.ribs.core.routing.backstack.ConfigurationContext.ActivationState.INACTIVE
import com.badoo.ribs.core.routing.backstack.ConfigurationContext.ActivationState.SLEEPING
import com.badoo.ribs.core.routing.backstack.ConfigurationContext.Resolved
import com.badoo.ribs.core.routing.backstack.ConfigurationContext.Unresolved
import com.badoo.ribs.core.routing.backstack.ConfigurationKey
import com.badoo.ribs.core.routing.backstack.action.ActionExecutionParams
import com.badoo.ribs.core.routing.backstack.action.single.AddAction
import com.badoo.ribs.core.routing.backstack.action.multi.MultiConfigurationAction
import com.badoo.ribs.core.routing.backstack.action.single.SingleConfigurationAction
import com.badoo.ribs.core.routing.backstack.feature.ConfigurationFeature.Effect
import io.reactivex.Observable
import io.reactivex.Observable.empty
import io.reactivex.Observable.fromCallable
import io.reactivex.Observable.fromIterable

private val timeCapsuleKey = ConfigurationFeature::class.java.name
private fun <C : Parcelable> TimeCapsule<SavedState<C>>.initialState(): WorkingState<C> =
    (get<SavedState<C>>(timeCapsuleKey)
        ?.let { it.toWorkingState() }
        ?: WorkingState())

/**
 * State store responsible for executing [ConfigurationCommand]s it takes as inputs.
 *
 * The [WorkingState] contains a pool of [ConfigurationContext] elements referenced
 * by [ConfigurationKey] objects. Practically, these keep reference to all configurations
 * currently associated with the RIB: all initial configurations (typically permanent parts
 * and one content type) + the ones coming from back stack changes.
 *
 * Any given [ConfigurationContext] in the pool can be typically in [ACTIVE] or [INACTIVE] state,
 * respective to whether it is active on the screen.
 * Last elements in the back stack are activated, others are deactivated.
 * Permanent parts are added and activated on initialisation and never deactivated as long as
 * the view is available.
 */
internal class ConfigurationFeature<C : Parcelable>(
    initialConfigurations: List<C>,
    timeCapsule: TimeCapsule<SavedState<C>>,
    resolver: (C) -> RoutingAction<*>,
    parentNode: Node<*>
) : ActorReducerFeature<ConfigurationCommand<C>, Effect<C>, WorkingState<C>, Nothing>(
    initialState = timeCapsule.initialState<C>(),
    bootstrapper = BootStrapperImpl(timeCapsule.initialState<C>(), initialConfigurations),
    actor = ActorImpl(resolver, parentNode),
    reducer = ReducerImpl()
) {
    init {
        timeCapsule.register(timeCapsuleKey) { state.toSavedState() }
    }

    sealed class Effect<C : Parcelable> {
        data class Global<C : Parcelable>(
            val command: MultiConfigurationCommand<C>,
            val updatedElements: Map<ConfigurationKey, Resolved<C>>
        ) : Effect<C>()

        data class Individual<C : Parcelable>(
            val command: SingleConfigurationCommand<C>,
            val updatedElement: Resolved<C>
        ) : Effect<C>()
    }

    /**
     * Automatically calls [Add] + [Activate] on all [initialConfigurations]
     */
    class BootStrapperImpl<C : Parcelable>(
        private val initialState: WorkingState<C>,
        private val initialConfigurations: List<C>
    ) : Bootstrapper<ConfigurationCommand<C>> {

        override fun invoke(): Observable<ConfigurationCommand<C>> =
            when {
                initialState.pool.isEmpty() -> fromIterable(
                    initialConfigurations
                        .mapIndexed { index, configuration ->
                            val key = ConfigurationKey.Permanent(index)

                            listOf<ConfigurationCommand<C>>(
                                Add(key, configuration),
                                Activate(key)
                            )
                        }
                        .flatten()
                    )
                else -> empty()
            }
    }

    /**
     * Executes [MultiConfigurationAction] / [SingleConfigurationAction] associated with the incoming
     * [ConfigurationCommand]. The actions will take care of [RoutingAction] invocations and [Node]
     * manipulations, and are expected to return updated elements.
     *
     * Updated elements are then passed on to the [ReducerImpl] in the respective [Effect]s
     */
    class ActorImpl<C : Parcelable>(
        private val resolver: (C) -> RoutingAction<*>,
        private val parentNode: Node<*>
    ) : Actor<WorkingState<C>, ConfigurationCommand<C>, Effect<C>> {
        override fun invoke(state: WorkingState<C>, command: ConfigurationCommand<C>): Observable<Effect<C>> =
            when (command) {
                is MultiConfigurationCommand ->
                    fromCallable {
                        command.action.execute(
                            pool = state.pool,
                            params = createParams(command, state)
                        )
                    }
                    .map { updated -> Effect.Global(command, updated) }

                is SingleConfigurationCommand ->
                    fromCallable {
                        command.action.execute(
                            key = command.key,
                            params = createParams(command, state)
                        )
                    }
                    .map { updated -> Effect.Individual(command, updated) }
            }

        private fun createParams(command: ConfigurationCommand<C>, state: WorkingState<C>): ActionExecutionParams<C> =
            ActionExecutionParams(
                resolver = { key ->
                    val defaultElement = when (command) {
                        is Add<C> -> Unresolved(INACTIVE, command.configuration)
                        else -> null
                    }

                    state.resolve(key, defaultElement)
                },
                parentNode = parentNode,
                globalActivationLevel = state.activationLevel
            )

        /**
         * Returns a [Resolved] [ConfigurationContext] looked up by [key].
         *
         * A [ConfigurationContext] should be already present in the pool either in already [Resolved],
         * or [Unresolved] form, the latter of which will be resolved on invocation.
         *
         * The only exception when it's acceptable not to already have an element under [key] is
         * when [defaultElement] is not null, used in the case of the [Add] command.
         */
        private fun WorkingState<C>.resolve(key: ConfigurationKey, defaultElement: Unresolved<C>?): Resolved<C> {
            val item = pool[key] ?: defaultElement ?: error("Key $key was not found in pool")

            return item.resolve(resolver, parentNode) {
                /**
                 * Resolution involves building the associated [Node]s, which need to be guaranteed
                 * to be added to the parentNode.
                 *
                 * Because of this, we need to make sure that [AddAction] is executed every time
                 * we resolve, even when no explicit [Add] command was asked.
                 *
                 * This is to cover cases e.g. when restoring from Bundle:
                 * we have a list of [Unresolved] elements that will be resolved on next command
                 * (e.g. [WakeUp] / [Activate]), by which time they will need to have been added.
                 *
                 * [Add] is only called explicitly with direct back stack manipulation, but not on
                 * state restoration.
                 */
                AddAction.execute(
                    it,
                    parentNode
                )
            }
        }
    }

    /**
     * Creates a new [WorkingState] based on the old one, plus the applied [Effect].
     *
     * Involves changing [WorkingState.activationLevel] in case of [Effect.Global],
     * and replacing elements of the [WorkingState.pool] changed by actions in [ActorImpl].
     */
    @SuppressWarnings("LongMethod")
    class ReducerImpl<C : Parcelable> : Reducer<WorkingState<C>, Effect<C>> {
        override fun invoke(state: WorkingState<C>, effect: Effect<C>): WorkingState<C> =
            when (effect) {
                is Effect.Global -> state.global(effect)
                is Effect.Individual -> state.individual(effect)
            }

        private fun WorkingState<C>.global(effect: Effect.Global<C>): WorkingState<C> =
            when (effect.command) {
                is Sleep -> copy(
                    activationLevel = SLEEPING,
                    pool = pool + effect.updatedElements
                )
                is WakeUp -> copy(
                    activationLevel = ACTIVE,
                    pool = pool + effect.updatedElements
                )
                is SaveInstanceState -> copy(
                    pool = pool + effect.updatedElements
                )
            }

        @SuppressWarnings("LongMethod")
        private fun WorkingState<C>.individual(effect: Effect.Individual<C>): WorkingState<C> {
            val key = effect.command.key
            val updated = effect.updatedElement

            return when (effect.command) {
                is Add -> copy(
                    pool = pool
                        .plus(key to updated)
                )
                is Activate,
                is Deactivate -> copy(
                    pool = pool
                        .minus(key)
                        .plus(key to updated)
                )
                is Remove -> copy(
                    pool = pool
                        .minus(key)
                )
            }
        }
    }
}
