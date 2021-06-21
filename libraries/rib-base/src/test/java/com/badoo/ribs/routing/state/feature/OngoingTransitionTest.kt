package com.badoo.ribs.routing.state.feature

import com.badoo.ribs.core.Rib
import com.badoo.ribs.core.helper.AnyConfiguration
import com.badoo.ribs.routing.state.feature.RoutingStatePool.Effect.Transition.TransitionFinished
import com.badoo.ribs.routing.state.feature.RoutingStatePool.Effect.Transition.TransitionStarted
import com.badoo.ribs.routing.transition.TransitionDirection
import com.badoo.ribs.routing.transition.TransitionElement
import com.badoo.ribs.routing.transition.progress.MultiProgressEvaluator
import com.badoo.ribs.routing.transition.progress.SingleProgressEvaluator
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.never
import com.nhaarman.mockitokotlin2.verify
import org.junit.Test
import java.util.UUID

class OngoingTransitionTest {

    private val effectEmitter: EffectEmitter<AnyConfiguration> = mock()
    private val transitionElement = TransitionElementStub()

    private val ongoingTransition = OngoingTransition(
        descriptor = mock(),
        actions = emptyList(),
        direction = transitionElement.direction,
        transitionPair = mock(),
        transitionElements = listOf(transitionElement),
        emitter = effectEmitter,
    )

    @Test
    fun `GIVEN there are pending transition elements WHEN transition has started THEN TransitionStarted is emitted but not TransitionFinished`() {
        ongoingTransition.start()

        verify(effectEmitter).invoke(any<TransitionStarted<AnyConfiguration>>())
        verify(effectEmitter, never()).invoke(any<TransitionFinished<AnyConfiguration>>())
    }

    @Test
    fun `GIVEN there are pending transition elements WHEN transition has finished THEN TransitionFinished is emitted`() {
        ongoingTransition.start()

        transitionElement.singleProgressEvaluator.markFinished()

        verify(effectEmitter).invoke(any<TransitionFinished<AnyConfiguration>>())
    }

    @Test
    fun `GIVEN there are NO pending transition elements WHEN transition has started THEN TransitionFinished is emitted`() {
        transitionElement.singleProgressEvaluator.markFinished()

        ongoingTransition.start()

        verify(effectEmitter).invoke(any<TransitionFinished<AnyConfiguration>>())
    }

    @Test
    fun `WHEN transition is jumpToEnd THEN TransitionFinished is emitted`() {
        ongoingTransition.jumpToEnd()

        verify(effectEmitter).invoke(any<TransitionFinished<AnyConfiguration>>())
    }

    private class TransitionElementStub : TransitionElement<AnyConfiguration>(
        configuration = AnyConfiguration,
        direction = TransitionDirection.ENTER,
        addedOrRemoved = false,
        identifier = Rib.Identifier(UUID.randomUUID()),
        view = mock(),
        progressEvaluator = MultiProgressEvaluator()
    ) {
        val singleProgressEvaluator = SingleProgressEvaluator()

        init {
            progressEvaluator.add(singleProgressEvaluator)
        }
    }

}
