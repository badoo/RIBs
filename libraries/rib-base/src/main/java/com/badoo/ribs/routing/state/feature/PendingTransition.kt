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

    fun schedule() {
        emitter.invoke(RoutingStatePool.Effect.RequestTransition(this))
    }

    fun execute(transitionHandler: TransitionHandler<C>) {
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
        emitter.invoke(RoutingStatePool.Effect.RemovePendingTransition(this))
    }

}