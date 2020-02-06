package com.badoo.ribs.core.routing.configuration.feature

import android.os.Parcelable
import com.badoo.mvicore.element.Bootstrapper
import com.badoo.mvicore.element.Reducer
import com.badoo.mvicore.element.TimeCapsule
import com.badoo.mvicore.feature.ActorReducerFeature
import com.badoo.ribs.core.Node
import com.badoo.ribs.core.routing.action.RoutingAction
import com.badoo.ribs.core.routing.configuration.ConfigurationCommand
import com.badoo.ribs.core.routing.configuration.ConfigurationCommand.Activate
import com.badoo.ribs.core.routing.configuration.ConfigurationCommand.Add
import com.badoo.ribs.core.routing.configuration.ConfigurationCommand.Deactivate
import com.badoo.ribs.core.routing.configuration.ConfigurationCommand.Remove
import com.badoo.ribs.core.routing.configuration.ConfigurationContext
import com.badoo.ribs.core.routing.configuration.ConfigurationContext.ActivationState.ACTIVE
import com.badoo.ribs.core.routing.configuration.ConfigurationContext.ActivationState.INACTIVE
import com.badoo.ribs.core.routing.configuration.ConfigurationContext.ActivationState.SLEEPING
import com.badoo.ribs.core.routing.configuration.ConfigurationContext.Resolved
import com.badoo.ribs.core.routing.configuration.ConfigurationKey
import com.badoo.ribs.core.routing.configuration.Transaction
import com.badoo.ribs.core.routing.configuration.Transaction.MultiConfigurationCommand
import com.badoo.ribs.core.routing.configuration.Transaction.MultiConfigurationCommand.SaveInstanceState
import com.badoo.ribs.core.routing.configuration.Transaction.MultiConfigurationCommand.Sleep
import com.badoo.ribs.core.routing.configuration.Transaction.MultiConfigurationCommand.WakeUp
import com.badoo.ribs.core.routing.configuration.feature.ConfigurationFeature.Effect
import com.badoo.ribs.core.routing.transition.handler.TransitionHandler
import io.reactivex.Observable
import io.reactivex.Observable.empty
import io.reactivex.Observable.fromIterable

// FIXME don't commit
var println = false

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
    parentNode: Node<*>,
    transitionHandler: TransitionHandler<C>?
) : ActorReducerFeature<Transaction<C>, Effect<C>, WorkingState<C>, Nothing>(
    initialState = timeCapsule.initialState<C>(),
    bootstrapper = BootStrapperImpl(timeCapsule.initialState<C>(), initialConfigurations),
    actor = ConfigurationFeatureActor(
        resolver,
        parentNode,
        transitionHandler
    ),
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
            val command: ConfigurationCommand<C>,
            val updatedElement: Resolved<C>
        ) : Effect<C>()

        data class TransitionStarted<C : Parcelable>(
            val transition: OngoingTransition<C>
        ) : Effect<C>()

        data class TransitionFinished<C : Parcelable>(
            val transition: OngoingTransition<C>
        ) : Effect<C>()
    }

    /**
     * Automatically calls [Add] + [Activate] on all [initialConfigurations]
     */
    class BootStrapperImpl<C : Parcelable>(
        private val initialState: WorkingState<C>,
        private val initialConfigurations: List<C>
    ) : Bootstrapper<Transaction<C>> {

        override fun invoke(): Observable<Transaction<C>> =
            when {
                initialState.pool.isEmpty() -> fromIterable(
                    initialConfigurations
                        .mapIndexed { index, configuration ->
                            val key = ConfigurationKey.Permanent(index)

                            Transaction.ListOfCommands<C>(
                                descriptor = TransitionDescriptor.None,
                                commands = listOf(
                                    Add(key, configuration),
                                    Activate(key)
                                )
                            )
                        }
                )
                else -> empty()
            }
    }

    /**
     * Creates a new [WorkingState] based on the old one, plus the applied [Effect].
     *
     * Involves changing [WorkingState.activationLevel] in case of [Effect.Global],
     * and replacing elements of the [WorkingState.pool] changed by actions in [ActorImpl].
     */
    class ReducerImpl<C : Parcelable> : Reducer<WorkingState<C>, Effect<C>> {
        override fun invoke(state: WorkingState<C>, effect: Effect<C>): WorkingState<C> =
            when (effect) {
                is Effect.Global -> state.global(effect)
                is Effect.Individual -> state.individual(effect)
                is Effect.TransitionStarted -> state.copy(ongoingTransitions = state.ongoingTransitions + effect.transition)
                is Effect.TransitionFinished -> state.copy(ongoingTransitions = state.ongoingTransitions - effect.transition)
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

        private fun WorkingState<C>.individual(effect: Effect.Individual<C>): WorkingState<C> {
            val key = effect.command.key
            val updated = effect.updatedElement

            return when (effect.command) {
                is Add -> {
                    if (println) println("Adding to pool: $key to $updated")
                    copy(
                        pool = pool.plus(key to updated)
                    )
                }
                is Activate,
                is Deactivate -> copy(
                    pool = pool.minus(key).plus(key to updated)
                )
                is Remove -> copy(
                    pool = pool.minus(key)
                )
            }
        }
    }
}
