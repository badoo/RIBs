package com.badoo.ribs.routing.state.feature

import android.os.Parcelable
import com.badoo.ribs.routing.state.action.single.ReversibleAction
import com.badoo.ribs.routing.state.changeset.TransitionDescriptor
import com.badoo.ribs.routing.transition.TransitionDirection
import com.badoo.ribs.routing.transition.TransitionElement

internal class PendingTransitionFactory<C : Parcelable>(
    private val effectEmitter: EffectEmitter<C>,
    private val internalTransactionConsumer: InternalTransactionConsumer<C>
) {

    fun create(
        descriptor: TransitionDescriptor,
        direction: TransitionDirection,
        actions: List<ReversibleAction<C>>,
        transitionElements: List<TransitionElement<C>>,
    ): PendingTransition<C> =
        PendingTransition(
            descriptor = descriptor,
            direction = direction,
            actions = actions,
            transitionElements = transitionElements,
            effectEmitter = effectEmitter,
            internalTransactionConsumer = internalTransactionConsumer
        )
}
