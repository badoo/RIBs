package com.badoo.ribs.routing.state.feature

import android.os.Handler
import android.os.Parcelable
import com.badoo.ribs.routing.state.action.single.ReversibleAction
import com.badoo.ribs.routing.state.changeset.TransitionDescriptor
import com.badoo.ribs.routing.transition.TransitionDirection
import com.badoo.ribs.routing.transition.TransitionElement
import com.badoo.ribs.routing.transition.TransitionPair

@SuppressWarnings("LongParameterList")
internal class OngoingTransition<C : Parcelable>(
    descriptor: TransitionDescriptor,
    val direction: TransitionDirection,
    private val transitionPair: TransitionPair,
    private var actions: List<ReversibleAction<C>>,
    private val transitionElements: List<TransitionElement<C>>,
    private val emitter: EffectEmitter<C>,
    private val handler: Handler = Handler()
) {

    var descriptor = descriptor
        private set

    private val checkFinishedRunnable = object : Runnable {
        override fun run() {
            if (transitionElements.any { it.isPending() }) {
                handler.post(this)
            } else {
                finish()
            }
        }
    }

    fun dispose() {
        handler.removeCallbacks(checkFinishedRunnable)
    }

    fun start() {
        actions.forEach { it.onTransition() }
        emitter.invoke(
            RoutingStatePool.Effect.Transition.TransitionStarted(
                this
            )
        )
        checkFinishedRunnable.run()
        transitionPair.exiting?.start()
        transitionPair.entering?.start()
    }

    private fun finish() {
        handler.removeCallbacks(checkFinishedRunnable)
        actions.forEach { it.onFinish() }
        emitter.invoke(
            RoutingStatePool.Effect.Transition.TransitionFinished(
                this
            )
        )
    }

    fun jumpToEnd() {
        transitionPair.exiting?.end()
        transitionPair.entering?.end()
        finish()
    }

    fun reverse() {
        transitionPair.exiting?.reverse()
        transitionPair.entering?.reverse()
        descriptor = descriptor.reverse()
        actions = actions.reversed()
        actions.forEach { it.reverse() }
    }
}
