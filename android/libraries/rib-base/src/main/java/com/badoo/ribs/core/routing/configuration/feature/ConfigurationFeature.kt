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
        sealed class Global<C : Parcelable>: Effect<C>() {
            class WakeUp<C : Parcelable>: Global<C>()
            class Sleep<C : Parcelable>: Global<C>()
            class SaveInstanceState<C : Parcelable>(
                val updatedElements: Map<ConfigurationKey, Resolved<C>>
            ): Global<C>()
        }

        sealed class Individual<C : Parcelable>: Effect<C>() {
            abstract val key: ConfigurationKey
            abstract val updatedElement: Resolved<C>

            class Added<C : Parcelable>(
                override val key: ConfigurationKey,
                override val updatedElement: Resolved<C>
            ) : Individual<C>()

            class Removed<C : Parcelable>(
                override val key: ConfigurationKey,
                override val updatedElement: Resolved<C>
            ) : Individual<C>()

            class Activated<C : Parcelable>(
                override val key: ConfigurationKey,
                override val updatedElement: Resolved<C>
            ) : Individual<C>()

            class Deactivated<C : Parcelable>(
                override val key: ConfigurationKey,
                override val updatedElement: Resolved<C>
            ) : Individual<C>()

        }

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
                                    Activate(key, configuration)
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
            when (effect) {
                is Effect.Global.Sleep -> copy(
                    activationLevel = SLEEPING
                )
                is Effect.Global.WakeUp -> copy(
                    activationLevel = ACTIVE
                )
                is Effect.Global.SaveInstanceState -> copy(
                    pool = pool + effect.updatedElements
                )
            }

        private fun WorkingState<C>.individual(effect: Effect.Individual<C>): WorkingState<C> {
            val key = effect.key
            val updated = effect.updatedElement

            return when (effect) {
                is Effect.Individual.Added -> copy(
                        pool = pool.plus(key to updated)
                    )
                is Effect.Individual.Removed -> copy(
                    pool = pool.minus(key)
                )
                is Effect.Individual.Activated,
                is Effect.Individual.Deactivated -> copy(
                    pool = pool.minus(key).plus(key to updated)
                )
            }
        }
    }

    override fun dispose() {
        super.dispose()
        state.ongoingTransitions.forEach {
            it.dispose()
        }
    }
}
