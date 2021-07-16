package com.badoo.ribs.routing.state.feature

import android.os.Handler
import android.os.Looper
import android.os.Parcelable
import android.view.View
import com.badoo.ribs.routing.state.action.single.ReversibleAction
import com.badoo.ribs.routing.state.changeset.TransitionDescriptor
import com.badoo.ribs.routing.transition.TransitionDirection
import com.badoo.ribs.routing.transition.TransitionElement
import com.badoo.ribs.routing.transition.handler.TransitionHandler

@SuppressWarnings("LongParameterList")
internal class PendingTransition<C : Parcelable>(
    val descriptor: TransitionDescriptor,
    private val actions: List<ReversibleAction<C>>,
    private val direction: TransitionDirection,
    private val transitionElements: List<TransitionElement<C>>,
    private val effectEmitter: EffectEmitter<C>,
    private val internalTransactionConsumer: InternalTransactionConsumer<C>,
    private val handler: Handler = Handler(Looper.getMainLooper())
) {

    fun schedule() {
        effectEmitter.invoke(RoutingStatePool.Effect.Transition.RequestTransition(this))
        /**
         * Entering views at this point are created but will be measured / laid out the next frame.
         * We need to base calculations in transition implementations based on their actual measurements,
         * but without them appearing just yet to avoid flickering.
         * Making them invisible, starting the transitions then making them visible achieves the above.
         */
        hideEnteringElements()
        handler.post {
            internalTransactionConsumer.invoke(Transaction.InternalTransaction.ExecutePendingTransition(this))
        }
    }

    fun execute(transitionHandler: TransitionHandler<C>): OngoingTransition<C> {
        val transitionPair = transitionHandler.onTransition(transitionElements)
        discard()
        // TODO consider whether splitting this two two instances (one per direction, so that
        //  enter and exit can be controlled separately) is better
        return OngoingTransition(
            descriptor = descriptor,
            direction = direction,
            transitionPair = transitionPair,
            actions = actions,
            transitionElements = transitionElements,
            emitter = effectEmitter
        )
    }

    fun completeWithoutTransition() {
        discard()
        actions.forEach { it.onTransition() }
        actions.forEach { it.onFinish() }
    }

    fun discard() {
        showEnteringElements()
        effectEmitter.invoke(RoutingStatePool.Effect.Transition.RemovePendingTransition(this))
    }

    fun cancel() {
        handler.removeCallbacksAndMessages(null)
        discard()
    }

    private fun List<TransitionElement<C>>.visibility(visibility: Int) {
        forEach {
            it.view.visibility = visibility
        }
    }

    private fun hideEnteringElements() {
        val enteringElements = transitionElements.filter { it.direction == TransitionDirection.ENTER }
        enteringElements.visibility(View.INVISIBLE)
    }

    private fun showEnteringElements() {
        val enteringElements = transitionElements.filter { it.direction == TransitionDirection.ENTER }
        enteringElements.visibility(View.VISIBLE)
    }
}
