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

    private val runnable = object : Runnable {
        override fun run() {
            actions.forEach { action ->
                if (action.transitionElements.all { it.isFinished() }) {
                    action.onPostTransition()
                    action.transitionElements.forEach { it.markProcessed() }
                }
            }
            if (transitionElements.any { it.isInProgress() }) {
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
        runnable.run()
        transitionPair.exiting?.start()
        transitionPair.entering?.start()
    }

    private fun finish() {
        handler.removeCallbacks(runnable)
        actions.forEach { it.onFinish() }
        emitter.emitEffect(
            ConfigurationFeature.Effect.TransitionFinished(
                this
            )
        )
        emitter.onComplete()
    }

    // TODO remove when reverse() and abandon() are impemented
    fun jumpToEnd() {
        transitionPair.exiting?.end()
        transitionPair.entering?.end()
        runnable.run()
    }

    fun reverse() {
        // TODO implement
        jumpToEnd()
    }

    fun abandon() {
        // TODO consider its progressEvaluator
        // TODO consider what happens later if reversed
        transitionPair.entering?.pause()
        finish()
        // TODO yes, the above will pause incoming shared element transition,
        //  new configuration's view should take over in some form
        //  or old view should receive new target
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
