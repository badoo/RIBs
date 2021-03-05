package com.badoo.ribs.routing.state.feature

import android.os.Handler
import android.os.Parcelable
import android.view.View
import com.badoo.ribs.routing.state.action.single.ReversibleAction
import com.badoo.ribs.routing.state.changeset.TransitionDescriptor
import com.badoo.ribs.routing.transition.TransitionDirection
import com.badoo.ribs.routing.transition.TransitionElement
import com.badoo.ribs.routing.transition.handler.TransitionHandler

internal class PendingTransition<C : Parcelable>(
    val descriptor: TransitionDescriptor,
    private val direction: TransitionDirection,
    private var actions: List<ReversibleAction<C>>,
    private val transitionElements: List<TransitionElement<C>>,
    private val emitter: EffectEmitter<C>) {

    private var isDiscarded = false

    fun schedule(handler: Handler, transitionHandler: TransitionHandler<C>) {
        emitter.invoke(RoutingStatePool.Effect.RequestTransition(this))

        /**
         * Entering views at this point are created but will be measured / laid out the next frame.
         * We need to base calculations in transition implementations based on their actual measurements,
         * but without them appearing just yet to avoid flickering.
         * Making them invisible, starting the transitions then making them visible achieves the above.
         */

        val enteringElements = transitionElements.filter { it.direction == TransitionDirection.ENTER }
        enteringElements.visibility(View.INVISIBLE)
        handler.post {
            enteringElements.visibility(View.VISIBLE)

            if (isDiscarded.not()) {
                consume(transitionHandler)
            }
        }
    }

    private fun consume(transitionHandler: TransitionHandler<C>) {
        discard()
        val transitionPair = transitionHandler.onTransition(transitionElements)
        // TODO consider whether splitting this two two instances (one per direction, so that
        //  enter and exit can be controlled separately) is better
        OngoingTransition(
            descriptor = descriptor,
            direction = direction,
            transitionPair = transitionPair,
            actions = actions,
            transitionElements = transitionElements,
            emitter = emitter
        ).start()
    }

    fun completeWithoutTransition() {
        discard()
        actions.forEach { it.onTransition() }
        actions.forEach { it.onFinish() }
    }

    fun discard() {
        isDiscarded = true
        emitter.invoke(RoutingStatePool.Effect.RemovePendingTransition(this))
    }

    private fun List<TransitionElement<C>>.visibility(visibility: Int) {
        forEach {
            it.view.visibility = visibility
        }
    }
}