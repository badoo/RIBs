package com.badoo.ribs.core.routing.backstack.feature

import android.os.Parcelable
import com.badoo.mvicore.element.Actor
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
import com.badoo.ribs.core.routing.backstack.ConfigurationContext.ActivationState.ACTIVE
import com.badoo.ribs.core.routing.backstack.ConfigurationContext.ActivationState.INACTIVE
import com.badoo.ribs.core.routing.backstack.ConfigurationContext.ActivationState.SLEEPING
import com.badoo.ribs.core.routing.backstack.ConfigurationContext.Resolved
import com.badoo.ribs.core.routing.backstack.ConfigurationContext.Unresolved
import com.badoo.ribs.core.routing.backstack.ConfigurationKey
import com.badoo.ribs.core.routing.backstack.feature.ConfigurationFeature.Effect
import io.reactivex.Observable

private val timeCapsuleKey = ConfigurationFeature::class.java.name
private fun <C : Parcelable> TimeCapsule<SavedState<C>>.initialState(): WorkingState<C> =
    (get<SavedState<C>>(timeCapsuleKey)
        ?.let { it.toWorkingState() }
        ?: WorkingState())

internal class ConfigurationFeature<C : Parcelable>(
    timeCapsule: TimeCapsule<SavedState<C>>,
    resolver: (C) -> RoutingAction<*>,
    parentNode: Node<*>
) : ActorReducerFeature<ConfigurationCommand<C>, Effect<C>, WorkingState<C>, Nothing>(
    initialState = timeCapsule.initialState<C>(),
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
                    val defaultElement = if (command is Add<C>) Unresolved(command.configuration) else null
                    val resolved = state.resolve(command.key, defaultElement)

                    Observable
                        .fromCallable { command.action.execute(resolved, parentNode) }
                        .map { Effect.Individual(command, resolved) as Effect<C> }
                }
            }

        private fun WorkingState<C>.resolve(key: ConfigurationKey, defaultElement: Unresolved<C>?): Resolved<C> =
            when (val item = pool[key] ?: defaultElement ?: error("Key $key was not found in pool")) {
                is Resolved -> item
                is Unresolved -> item.resolve(resolver, parentNode)
            }
    }

    class ReducerImpl<C : Parcelable> : Reducer<WorkingState<C>, Effect<C>> {
        override fun invoke(state: WorkingState<C>, effect: Effect<C>): WorkingState<C> =
            when (effect) {
                is Effect.Global -> when (effect.command) {
                    is Sleep -> state.copy(
                        activationLevel = SLEEPING,
                        pool = state.pool.map { it.key to it.value.sleep() }.toMap()
                    )
                    is WakeUp -> state.copy(
                        activationLevel = ACTIVE,
                        pool = state.pool.map { it.key to it.value.wakeUp() }.toMap()
                    )
                }

                is Effect.Individual -> {
                    val key = effect.command.key
                    val element = effect.resolvedConfigurationContext

                    when (effect.command) {
                        is Add -> state.copy(
                            pool = state.pool
                                .plus(key to element)
                        )
                        is Activate -> state.copy(
                            pool = state.pool
                                .minus(key)
                                .plus(key to element.withActivationState(state.activationLevel))
                        )
                        is Deactivate -> state.copy(
                            pool = state.pool
                                .minus(key)
                                .plus(key to element.withActivationState(INACTIVE))
                        )
                        is Remove -> state.copy(
                            pool = state.pool
                                .minus(key)
                        )
                    }
                }
            }
    }
}
