package com.badoo.ribs.core.routing.configuration.feature

import android.os.Handler
import android.os.Parcelable
import com.badoo.ribs.core.routing.configuration.action.single.Action
import com.badoo.ribs.core.routing.transition.TransitionDirection
import com.badoo.ribs.core.routing.transition.TransitionElement
import com.badoo.ribs.core.routing.transition.TransitionPair
import io.reactivex.ObservableEmitter

internal class OngoingTransition<C : Parcelable>(
    val descriptor: TransitionDescriptor,
    val direction: TransitionDirection,
    private val transitionPair: TransitionPair,
    private val actions: List<Action<C>>,
    private val transitionElements: List<TransitionElement<C>>,
    private val emitter: ObservableEmitter<List<ConfigurationFeature.Effect<C>>>
) {
    private val handler = Handler()

    private val checkFinishedRunnable = object : Runnable {
        override fun run() {
            if (transitionElements.any { it.isPending() }) {
                handler.post(this)
            } else {
                finish()
            }
        }
    }

    fun start() {
        actions.forEach { it.onTransition() }
        emitter.emitEffect(
            ConfigurationFeature.Effect.TransitionStarted(
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
        emitter.emitEffect(
            ConfigurationFeature.Effect.TransitionFinished(
                this
            )
        )
        emitter.onComplete()
    }

    fun jumpToEnd() {
        transitionPair.exiting?.end()
        transitionPair.entering?.end()
        checkFinishedRunnable.run()
    }

    fun reverse() {
        transitionPair.exiting?.reverse()
        transitionPair.entering?.reverse()
        actions.forEach { it.reverse() }
    }

    private fun ObservableEmitter<List<ConfigurationFeature.Effect<C>>>.emitEffect(
        effect: ConfigurationFeature.Effect<C>
    ) {
        onNext(
            listOf(
                effect
            )
        )
    }
}
