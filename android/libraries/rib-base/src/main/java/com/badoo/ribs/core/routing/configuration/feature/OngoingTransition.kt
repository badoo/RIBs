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
                // TODO consider removing onPostTransition + processed state
                if (action.transitionElements.all { it.isFinished() || it.isReset() }) {
                    action.onPostTransition()
                    action.transitionElements.forEach { it.markProcessed() }
                }
            }
            // FIXME check: after reverse it goes bck to 0.0, make sure else branch gets called
            // FIXME initial run with all Initialised goes straight to else?
            //  needs to have info on reverse:
            //  not reversed and all initialised or any in progress: wait
            //  reversed and all initialised: finish
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

    fun jumpToEnd() {
        transitionPair.exiting?.end()
        transitionPair.entering?.end()
        runnable.run()
    }

    // TODO consider splitting to two different methods + split actions and other stuff
    //  so that reverse can be detected and triggered separately for exiting and entering things
    //  (maybe even split OngoingTransition to 2 instances, each with Transition instead of Pair)
    //  also consider why this is maybe wrong altogether
    fun reverse() {
        transitionPair.exiting?.reverse()
        transitionPair.entering?.reverse()
        actions.forEach { it.reverse() }
    }

    fun abandon() {
        // TODO consider its progressEvaluator
        // TODO consider what happens later if reversed
        transitionPair.entering?.pause()
        finish()
        // TODO yes, the above will pause incoming transitions,
        //  leaving them on the screen (visual bug)
        //  new configuration's view should take over in some form
        //  or old view should receive new target
        //  -
        //  idea: some meta info registry entry that survives individual transitions if
        //    they are abandoned for another
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