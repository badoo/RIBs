package com.badoo.ribs.core.routing.configuration.action.single

import android.os.Parcelable
import com.badoo.ribs.core.Node
import com.badoo.ribs.core.routing.configuration.action.ActionExecutionParams
import com.badoo.ribs.core.routing.configuration.action.single.ReversibleActionPair.Direction.FORWARD
import com.badoo.ribs.core.routing.configuration.action.single.ReversibleActionPair.Direction.REVERSED
import com.badoo.ribs.core.routing.transition.TransitionElement

internal data class ReversibleActionPair<T : Parcelable>(
    private val forwardAction: Action<T>,
    private val reverseAction: Action<T>
) : ReversibleAction<T> {

    class Factory(
        private val nodeFilter: (Node.Descriptor) -> Boolean = { true },
        private val forwardActionFactory: ActionFactory,
        private val reverseActionFactory: ActionFactory
    ) : ReversibleActionFactory {
        override fun <C : Parcelable> create(params: ActionExecutionParams<C>): ReversibleAction<C> {
            val nodes = params.item.nodes.filter(nodeFilter).map { it.node }

            return ReversibleActionPair(
                forwardAction = forwardActionFactory.create(params, nodes),
                reverseAction = reverseActionFactory.create(params, nodes)
            )
        }
    }

    private enum class Direction {
        FORWARD, REVERSED;

        fun reverse(): Direction =
            when (this) {
                FORWARD -> REVERSED
                REVERSED -> FORWARD
            }
    }

    private var direction: Direction = FORWARD

    override val canExecute: Boolean
        get() = forwardAction.canExecute

    private val forceExecute: Boolean
        // If forwardAction is doing its thing, reverseAction should also disregard its own canExecute flag
        get() = direction == REVERSED && forwardAction.canExecute

    private val activeAction: Action<T>
        get() = when (direction) {
            FORWARD -> forwardAction
            REVERSED -> reverseAction
        }

    override fun reverse() {
        direction = direction.reverse()
        onTransition(forceExecute)
    }

    override fun onBeforeTransition() {
        activeAction.onBeforeTransition()
    }

    // TODO consider having this as return value of onBeforeTransition
    override val transitionElements: List<TransitionElement<T>>
        // It's always the default direction that defines the Transition
        // It will be reversed in the derived ValueAnimator, not here
        get() = forwardAction.transitionElements

    override fun onTransition(forceExecute: Boolean) {
        activeAction.onTransition(forceExecute || this.forceExecute)
    }

    override fun onFinish(forceExecute: Boolean) {
        activeAction.onFinish(forceExecute || this.forceExecute)
    }
}
