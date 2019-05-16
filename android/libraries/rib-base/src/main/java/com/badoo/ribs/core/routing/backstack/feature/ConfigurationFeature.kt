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
import com.badoo.ribs.core.routing.backstack.action.MultiConfigurationAction
import com.badoo.ribs.core.routing.backstack.action.SingleConfigurationAction
import com.badoo.ribs.core.routing.backstack.feature.ConfigurationFeature.Effect
import io.reactivex.Observable

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
 * currently associated with the RIB: Permanent parts + configurations coming from the back stack.
 *
 * Any given [ConfigurationContext] in the pool can be typically in [ACTIVE] or [INACTIVE] state,
 * respective to whether it is active on the screen.
 * Last elements in the back stack are activated, others are deactivated.
 * Permanent parts are added and activated on initialisation and never deactivated as long as
 * the view is available.
 */
internal class ConfigurationFeature<C : Parcelable>(
    permanentParts: List<C>,
    timeCapsule: TimeCapsule<SavedState<C>>,
    resolver: (C) -> RoutingAction<*>,
    parentNode: Node<*>
) : ActorReducerFeature<ConfigurationCommand<C>, Effect<C>, WorkingState<C>, Nothing>(
    initialState = timeCapsule.initialState<C>(),
    bootstrapper = BootStrapperImpl(permanentParts),
    actor = ActorImpl(resolver, parentNode),
    reducer = ReducerImpl()
) {
    init {
        timeCapsule.register(timeCapsuleKey) { state.toSavedState() }
    }

    sealed class Effect<C : Parcelable> {
        data class Global<C : Parcelable>(
            val command: MultiConfigurationCommand<C>
        ) : Effect<C>()

        data class Individual<C : Parcelable>(
            val command: SingleConfigurationCommand<C>,
            val resolvedConfigurationContext: Resolved<C>
        ) : Effect<C>()
    }

    /**
     * Automatically calls [Add] + [Activate] on all [permanentParts]
     */
    class BootStrapperImpl<C : Parcelable>(
        private val permanentParts: List<C>
    ) : Bootstrapper<ConfigurationCommand<C>> {

        override fun invoke(): Observable<ConfigurationCommand<C>> = Observable
            .fromIterable(
                permanentParts
                    .mapIndexed { index, configuration ->
                        val key = ConfigurationKey.Permanent(index)

                        listOf<ConfigurationCommand<C>>(
                            Add(key, configuration),
                            Activate(key)
                        )
                    }
                    .flatten()
            )
    }

    /**
     * Executes [MultiConfigurationAction] / [SingleConfigurationAction] associated with the incoming
     * [ConfigurationCommand]. The actions will take care of [RoutingAction] invocations and [Node]
     * manipulations.
     */
    class ActorImpl<C : Parcelable>(
        private val resolver: (C) -> RoutingAction<*>,
        private val parentNode: Node<*>
    ) : Actor<WorkingState<C>, ConfigurationCommand<C>, Effect<C>> {
        override fun invoke(state: WorkingState<C>, command: ConfigurationCommand<C>): Observable<Effect<C>> =
            when (command) {
                is MultiConfigurationCommand -> Observable
                        .fromCallable { command.action.execute(state.pool, parentNode) }
                        .map { Effect.Global(command) as Effect<C> }

                is SingleConfigurationCommand -> {
                    val defaultElement = when (command) {
                        is Add<C> -> Unresolved(INACTIVE, command.configuration)
                        else -> null
                    }
                    val resolved = state.resolve(command.key, defaultElement)

                    Observable
                        .fromCallable { command.action.execute(resolved, parentNode) }
                        .map { Effect.Individual(command, resolved) as Effect<C> }
                }
            }

        /**
         * Returns a [Resolved] [ConfigurationContext] looked up by [key].
         *
         * A [ConfigurationContext] should be already present in the pool either in already [Resolved],
         * or [Unresolved] form, the latter of which will be resolved on invocation.
         *
         * The only exception when it's acceptable not to already have an element under [key] is
         * when [defaultElement] is not null, used in the case of the [Add] command.
         */
        private fun WorkingState<C>.resolve(key: ConfigurationKey, defaultElement: Unresolved<C>?): Resolved<C> =
            when (val item = pool[key] ?: defaultElement ?: error("Key $key was not found in pool")) {
                is Resolved -> item
                is Unresolved -> item.resolve(resolver, parentNode)
            }
    }

    /**
     * Creates a new [WorkingState] based on the old one, plus the applied [Effect].
     *
     * Involves changing [WorkingState.activationLevel] in case of [Effect.Global],
     * and changing elements of the [WorkingState.pool] in the case of [Unresolved] -> [Resolved]
     * transitions and individual [Resolved.activationState] changes.
     */
    @SuppressWarnings("LongMethod")
    class ReducerImpl<C : Parcelable> : Reducer<WorkingState<C>, Effect<C>> {
        override fun invoke(state: WorkingState<C>, effect: Effect<C>): WorkingState<C> =
            when (effect) {
                is Effect.Global -> state.global(effect)
                is Effect.Individual -> state.individual(effect)
            }

        private fun WorkingState<C>.global(effect: Effect.Global<C>): WorkingState<C> {
            return when (effect.command) {
                is Sleep -> copy(
                    activationLevel = SLEEPING,
                    pool = pool.map { it.key to it.value.sleep() }.toMap()
                )
                is WakeUp -> copy(
                    activationLevel = ACTIVE,
                    pool = pool.map { it.key to it.value.wakeUp() }.toMap()
                )
            }
        }

        @SuppressWarnings("LongMethod")
        private fun WorkingState<C>.individual(effect: Effect.Individual<C>): WorkingState<C> {
            val key = effect.command.key
            val element = effect.resolvedConfigurationContext

            return when (effect.command) {
                is Add -> copy(
                    pool = pool
                        .plus(key to element)
                )
                /**
                 * Sets the [ConfigurationContext.activationState] of the element in question
                 * to the global [WorkingState.activationLevel] value, so that if
                 * the global state is [SLEEPING], the individual element cannot go
                 * directly to [ACTIVE] - that will be done on the next [WakeUp] command)
                 */
                is Activate -> copy(
                    pool = pool
                        .minus(key)
                        .plus(key to element.withActivationState(activationLevel))
                )
                is Deactivate -> copy(
                    pool = pool
                        .minus(key)
                        .plus(key to element.withActivationState(INACTIVE))
                )
                is Remove -> copy(
                    pool = pool
                        .minus(key)
                )
            }
        }
    }
}
