package com.badoo.ribs.core.routing.backstack

import android.os.Bundle
import android.os.Parcelable
import com.badoo.mvicore.element.Actor
import com.badoo.mvicore.element.Reducer
import com.badoo.mvicore.element.TimeCapsule
import com.badoo.mvicore.feature.ActorReducerFeature
import com.badoo.ribs.core.Node
import com.badoo.ribs.core.routing.action.RoutingAction
import com.badoo.ribs.core.routing.backstack.ConfigurationCommand.Global.Sleep
import com.badoo.ribs.core.routing.backstack.ConfigurationCommand.Global.WakeUp
import com.badoo.ribs.core.routing.backstack.ConfigurationCommand.Individual.Activate
import com.badoo.ribs.core.routing.backstack.ConfigurationCommand.Individual.Add
import com.badoo.ribs.core.routing.backstack.ConfigurationCommand.Individual.Deactivate
import com.badoo.ribs.core.routing.backstack.ConfigurationCommand.Individual.Remove
import com.badoo.ribs.core.routing.backstack.ConfigurationContext.ActivationState
import com.badoo.ribs.core.routing.backstack.ConfigurationContext.ActivationState.ACTIVE
import com.badoo.ribs.core.routing.backstack.ConfigurationContext.ActivationState.INACTIVE
import com.badoo.ribs.core.routing.backstack.ConfigurationContext.ActivationState.SLEEPING
import com.badoo.ribs.core.routing.backstack.ConfigurationContext.Resolved
import com.badoo.ribs.core.routing.backstack.ConfigurationContext.Unresolved
import com.badoo.ribs.core.routing.backstack.ConfigurationFeature.Effect
import com.badoo.ribs.core.routing.backstack.ConfigurationFeature.State
import io.reactivex.Observable
import kotlinx.android.parcel.Parcelize

internal class ConfigurationFeature<C : Parcelable>(
    timeCapsule: TimeCapsule<SavedState<C>>,
    resolver: (C) -> RoutingAction<*>,
    parentNode: Node<*>
) : ActorReducerFeature<ConfigurationCommand<C>, Effect<C>, State<C>, Nothing>(
    initialState = timeCapsule.get<SavedState<C>>(ConfigurationFeature::class.java.name)?.let { State.from(it) } ?: State(
        activationLevel = SLEEPING
    ),
    actor = ActorImpl(resolver, parentNode),
    reducer = ReducerImpl()
) {
    init {
        timeCapsule.register(ConfigurationFeature::class.java.name) { state.toSavedState() }
    }

    @Parcelize
    data class SavedState<C : Parcelable>(
        val pool: Map<ConfigurationKey, Unresolved<C>>
    ) : Parcelable

    data class State<C : Parcelable>(
        val activationLevel: ActivationState,
        val pool: Map<ConfigurationKey, ConfigurationContext<C>> = mapOf()
    ) {
        fun toSavedState() = SavedState(
            pool.map {
                it.key to when (val entry = it.value) {
                    is Unresolved -> entry
                    is Resolved -> entry.shrink()
                }.copy(
                    activationState = SLEEPING
                )
            }.toMap()
        )

        companion object {
            fun <C : Parcelable> from(savedState: SavedState<C>): State<C> =
                State(SLEEPING, savedState.pool)
        }
    }

    sealed class Effect<C : Parcelable> {
        data class Global<C : Parcelable>(val command: ConfigurationCommand.Global<C>) : Effect<C>()
        data class Individual<C : Parcelable>(
            val command: ConfigurationCommand.Individual<C>,
            val resolvedConfigurationContext: Resolved<C>
        ) : Effect<C>()
    }

    class ActorImpl<C : Parcelable>(
        private val resolver: (C) -> RoutingAction<*>,
        private val parentNode: Node<*>
    ) : Actor<State<C>, ConfigurationCommand<C>, Effect<C>> {
        override fun invoke(state: State<C>, wish: ConfigurationCommand<C>): Observable<Effect<C>> =
            when (wish) {
                is ConfigurationCommand.Global -> Observable
                        .fromCallable { state.applyCommand(wish) }
                        .map { Effect.Global(wish) as Effect<C> }

                is ConfigurationCommand.Individual -> {
                    val defaultElement = if (wish is Add<C>) Unresolved(wish.configuration) else null
                    val resolved = state.resolve(wish.key, defaultElement)

                    Observable
                        .fromCallable { resolved.applyCommand(wish) }
                        .map { Effect.Individual(wish, resolved) as Effect<C> }
                }
            }

        private fun State<C>.applyCommand(command: ConfigurationCommand.Global<C>) {
            when (command) {
                is Sleep -> invokeOnResolved(ACTIVE) { it.deactivate() }
                is WakeUp -> invokeOnResolved(SLEEPING) { it.activate() }
            }
        }

        private fun State<C>.invokeOnResolved(activationState: ActivationState, block: (Resolved<C>) -> Unit) {
            pool
                .filter { it.value is Resolved<*> && it.value.activationState == activationState }
                .map { it.key to it.value as Resolved<C> }
                .forEach {
                    val (key, entry) = it
                    block.invoke(entry)
                }
        }

        private fun Resolved<C>.applyCommand(command: ConfigurationCommand.Individual<C>) {
            when (command) {
                is Add -> { /* nop here, resolve() adds automatically (needed after restoration) */ }
                is Activate -> activate()
                is Deactivate -> deactivate()
                is Remove -> destroyChildren()
            }
        }

        private fun State<C>.resolve(key: ConfigurationKey, defaultElement: Unresolved<C>?): Resolved<C> =
            when (val item = pool[key] ?: defaultElement ?: error("Key $key was not found in pool")) {
                is Resolved -> item
                is Unresolved -> {
                    val routingAction = resolver.invoke(item.configuration)
                    val resolved = Resolved(
                        configuration = item.configuration,
                        routingAction = routingAction,
                        builtNodes = routingAction.buildNodes(),
                        bundles = emptyList(),
                        activationState = INACTIVE
                    ).also {
                        it.attachChildNodesToParent()
                    }

                    resolved
                }
            }

        private fun Resolved<C>.attachChildNodesToParent() {
            builtNodes.forEachIndexed { index, nodeDescriptor ->
                parentNode.attachChildNode(nodeDescriptor.node, bundleAt(index))
            }
        }

        private fun Resolved<C>.bundleAt(index: Int): Bundle? =
            bundles.elementAtOrNull(index)?.also {
                it.classLoader = ConfigurationContext::class.java.classLoader
            }

        private fun Resolved<C>.activate() {
            reAttachViewsIfNeeded()
            routingAction.execute()
        }

        private fun Resolved<C>.reAttachViewsIfNeeded() {
            builtNodes
                .forEach {
                    if (it.viewAttachMode == Node.ViewAttachMode.PARENT && !it.node.isViewAttached) {
                        parentNode.attachChildView(it.node)
                    }
                }
        }

        private fun Resolved<C>.deactivate() {
            routingAction.cleanup()
            saveAndDetachChildViews()
        }

        private fun Resolved<C>.saveAndDetachChildViews() {
            builtNodes.forEach {
                it.node.saveViewState()

                if (it.viewAttachMode == Node.ViewAttachMode.PARENT) {
                    parentNode.detachChildView(it.node)
                }
            }
        }

        private fun Resolved<C>.destroyChildren() {
            builtNodes.forEach {
                parentNode.detachChildView(it.node)
                parentNode.detachChildNode(it.node)
            }
        }
    }

    class ReducerImpl<C : Parcelable> : Reducer<State<C>, Effect<C>> {
        override fun invoke(state: State<C>, effect: Effect<C>): State<C> =
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
                                .plus(key to element.setActivationState(state.activationLevel))
                        )
                        is Deactivate -> state.copy(
                            pool = state.pool
                                .minus(key)
                                .plus(key to element.setActivationState(INACTIVE))
                        )
                        is Remove -> state.copy(
                            pool = state.pool
                                .minus(key)
                        )
                    }
                }
            }

        private fun ConfigurationContext<C>.sleep(): ConfigurationContext<C> =
            transition(ACTIVE, SLEEPING)

        private fun ConfigurationContext<C>.wakeUp(): ConfigurationContext<C> =
            transition(SLEEPING, ACTIVE)

        private fun ConfigurationContext<C>.transition(from: ActivationState, to: ActivationState): ConfigurationContext<C> =
            if (activationState != from) {
                this
            } else {
                setActivationState(to)
            }

        private fun ConfigurationContext<C>.setActivationState(to: ActivationState): ConfigurationContext<C> =
            when (this) {
                is Unresolved -> copy(activationState = to)
                is Resolved -> copy(activationState = to)
            }
    }
}
