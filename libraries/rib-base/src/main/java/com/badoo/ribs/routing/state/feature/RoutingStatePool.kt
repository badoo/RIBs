package com.badoo.ribs.routing.state.feature

import android.os.Parcelable
import com.badoo.ribs.annotation.OutdatedDocumentation
import com.badoo.ribs.core.Node
import com.badoo.ribs.minimal.state.AsyncStore
import com.badoo.ribs.minimal.state.TimeCapsule
import com.badoo.ribs.routing.Routing
import com.badoo.ribs.routing.activator.RoutingActivator
import com.badoo.ribs.routing.resolver.RoutingResolver
import com.badoo.ribs.routing.state.RoutingContext
import com.badoo.ribs.routing.state.RoutingContext.ActivationState.ACTIVE
import com.badoo.ribs.routing.state.RoutingContext.ActivationState.INACTIVE
import com.badoo.ribs.routing.state.RoutingContext.ActivationState.SLEEPING
import com.badoo.ribs.routing.state.RoutingContext.Resolved
import com.badoo.ribs.routing.state.changeset.RoutingCommand.Add
import com.badoo.ribs.routing.state.changeset.TransitionDescriptor
import com.badoo.ribs.routing.state.feature.RoutingStatePool.Effect
import com.badoo.ribs.routing.state.feature.state.SavedState
import com.badoo.ribs.routing.state.feature.state.WorkingState
import com.badoo.ribs.routing.transition.handler.TransitionHandler

private val timeCapsuleKey = RoutingStatePool::class.java.name
private fun <C : Parcelable> TimeCapsule.initialState(): WorkingState<C> =
    (get<SavedState<C>>(timeCapsuleKey)?.toWorkingState()
        ?: WorkingState())

/**
 * State store responsible for executing [Transaction]s.
 *
 * The [WorkingState] contains a pool of [RoutingContext] elements referenced
 * by [Routing] objects. Practically, these keep reference to all configurations
 * currently associated with the RIB.
 */
@OutdatedDocumentation
@Suppress("LongParameterList")
internal open class RoutingStatePool<C : Parcelable>(
    timeCapsule: TimeCapsule,
    resolver: RoutingResolver<C>,
    activator: RoutingActivator<C>,
    parentNode: Node<*>,
    transitionHandler: TransitionHandler<C>?
) : AsyncStore<Effect<C>, WorkingState<C>>(initialState = timeCapsule.initialState<C>()) {

    private val actor = Actor(
        resolver = resolver,
        activator = activator,
        parentNode = parentNode,
        transitionHandler = transitionHandler,
        effectEmitter = ::emitEvent,
        pendingTransitionFactory = PendingTransitionFactory(::emitEvent, ::consumeInternalTransaction)
    )

    init {
        initialize()
        timeCapsule.register(timeCapsuleKey) { state.toSavedState() }
    }

    sealed class Effect<C : Parcelable> {
        sealed class Global<C : Parcelable> : Effect<C>() {
            class WakeUp<C : Parcelable> : Global<C>()
            class Sleep<C : Parcelable> : Global<C>()
            class SaveInstanceState<C : Parcelable>(
                val updatedElements: Map<Routing<C>, Resolved<C>>
            ) : Global<C>()
        }

        sealed class Individual<C : Parcelable> : Effect<C>() {
            abstract val key: Routing<C>

            class Added<C : Parcelable>(
                override val key: Routing<C>,
                val updatedElement: Resolved<C>
            ) : Individual<C>()

            class Removed<C : Parcelable>(
                override val key: Routing<C>,
                val updatedElement: Resolved<C>
            ) : Individual<C>()

            class Activated<C : Parcelable>(
                override val key: Routing<C>,
                val updatedElement: Resolved<C>
            ) : Individual<C>()

            class Deactivated<C : Parcelable>(
                override val key: Routing<C>,
                val updatedElement: Resolved<C>
            ) : Individual<C>()

            class PendingDeactivateTrue<C : Parcelable>(
                override val key: Routing<C>
            ) : Individual<C>()

            class PendingDeactivateFalse<C : Parcelable>(
                override val key: Routing<C>
            ) : Individual<C>()

            class PendingRemovalTrue<C : Parcelable>(
                override val key: Routing<C>
            ) : Individual<C>()

            class PendingRemovalFalse<C : Parcelable>(
                override val key: Routing<C>
            ) : Individual<C>()
        }

        sealed class Transition<C : Parcelable> : Effect<C>() {
            class RequestTransition<C : Parcelable>(
                val pendingTransition: PendingTransition<C>
            ) : Transition<C>()

            class RemovePendingTransition<C : Parcelable>(
                val pendingTransition: PendingTransition<C>
            ) : Transition<C>()

            data class TransitionStarted<C : Parcelable>(
                val transition: OngoingTransition<C>
            ) : Transition<C>()

            data class TransitionFinished<C : Parcelable>(
                val transition: OngoingTransition<C>
            ) : Transition<C>()
        }
    }

    private fun initialize() {
        if (state.pool.isNotEmpty()) {
            accept(
                Transaction.RoutingChange(
                    descriptor = TransitionDescriptor.None,
                    changeset = state.pool
                        .filter { it.value.activationState == INACTIVE || it.value.activationState == SLEEPING }
                        .map { Add(it.key) }
                )
            )
        }
    }

    fun accept(transaction: Transaction<C>) {
        actor.invoke(state, transaction)
    }

    private fun consumeInternalTransaction(internalTransaction: Transaction.InternalTransaction<C>) {
        accept(internalTransaction)
    }

    override fun reduceEvent(effect: Effect<C>, state: WorkingState<C>): WorkingState<C> =
        when (effect) {
            is Effect.Global -> state.global(effect)
            is Effect.Individual -> state.individual(effect)
            is Effect.Transition -> state.transition(effect)
        }

    private fun WorkingState<C>.transition(effect: Effect.Transition<C>): WorkingState<C> =
        when (effect) {
            is Effect.Transition.RequestTransition -> copy(pendingTransitions = pendingTransitions + effect.pendingTransition)
            is Effect.Transition.RemovePendingTransition -> copy(pendingTransitions = pendingTransitions - effect.pendingTransition)
            is Effect.Transition.TransitionStarted -> copy(ongoingTransitions = ongoingTransitions + effect.transition)
            is Effect.Transition.TransitionFinished -> copy(ongoingTransitions = ongoingTransitions - effect.transition)
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

        return when (effect) {
            is Effect.Individual.Added -> copy(
                pool = pool.plus(key to effect.updatedElement)
            )
            is Effect.Individual.Removed -> copy(
                pool = pool.minus(key)
            )
            is Effect.Individual.Activated -> copy(
                pool = pool.minus(key).plus(key to effect.updatedElement)
            )
            is Effect.Individual.Deactivated -> copy(
                pool = pool.minus(key).plus(key to effect.updatedElement)
            )
            is Effect.Individual.PendingDeactivateTrue -> copy(
                pendingDeactivate = pendingDeactivate + effect.key
            )
            is Effect.Individual.PendingDeactivateFalse -> copy(
                pendingDeactivate = pendingDeactivate - effect.key
            )
            is Effect.Individual.PendingRemovalTrue -> copy(
                pendingRemoval = pendingRemoval + effect.key
            )
            is Effect.Individual.PendingRemovalFalse -> copy(
                pendingRemoval = pendingRemoval - effect.key
            )
        }
    }

    override fun cancel() {
        super.cancel()
        disposeOngoingTransitions(state.ongoingTransitions)
        cancelPendingTransitions(state.pendingTransitions)
    }

    private fun disposeOngoingTransitions(ongoingTransitions: List<OngoingTransition<C>>) {
        ongoingTransitions.forEach {
            it.dispose()
        }
    }

    private fun cancelPendingTransitions(pendingTransition: List<PendingTransition<C>>) {
        pendingTransition.forEach {
            it.cancel()
        }
    }
}
