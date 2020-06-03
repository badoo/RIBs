package com.badoo.ribs.core.routing.state.feature

import android.os.Handler
import android.os.Parcelable
import com.badoo.ribs.core.routing.state.action.single.ReversibleAction
import com.badoo.ribs.core.routing.transition.TransitionDirection
import com.badoo.ribs.core.routing.transition.TransitionElement
import com.badoo.ribs.core.routing.transition.TransitionPair
import com.badoo.ribs.core.routing.state.transaction.TransitionDescriptor

internal class OngoingTransition<C : Parcelable>(
    descriptor: TransitionDescriptor,
    val direction: TransitionDirection,
    private val transitionPair: TransitionPair,
    private var actions: List<ReversibleAction<C>>,
    private val transitionElements: List<TransitionElement<C>>,
    private val emitter: EffectEmitter<C>
) {
    private val handler = Handler()
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
        emitter.onNext(
            RoutingStatePool.Effect.TransitionStarted(
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
        emitter.onNext(
            RoutingStatePool.Effect.TransitionFinished(
                this
            )
        )
        emitter.onComplete()
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
