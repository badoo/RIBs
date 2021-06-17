package com.badoo.ribs.routing.state.feature

import android.os.Parcelable
import com.badoo.ribs.minimal.reactive.Cancellable
import com.badoo.ribs.minimal.reactive.Source
import com.badoo.ribs.minimal.reactive.combineLatest
import com.badoo.ribs.minimal.reactive.distinctUntilChanged
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
    transitionElements: List<TransitionElement<C>>,
    private val emitter: EffectEmitter<C>,
) {

    var descriptor: TransitionDescriptor = descriptor
        private set

    private val isAnyPending: Source<Boolean> =
        combineLatest(transitionElements.map { it.isPendingSource }) { array -> array.any { it == true } }
            .distinctUntilChanged()

    private var cancellable: Cancellable? = null

    fun dispose() {
        cancellable?.cancel()
    }

    fun start() {
        actions.forEach { it.onTransition() }
        emitter.invoke(
            RoutingStatePool.Effect.Transition.TransitionStarted(
                this
            )
        )
        cancellable?.cancel()
        cancellable = isAnyPending.observe { if (!it) finish() }
        transitionPair.exiting?.start()
        transitionPair.entering?.start()
    }

    private fun finish() {
        dispose()
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
