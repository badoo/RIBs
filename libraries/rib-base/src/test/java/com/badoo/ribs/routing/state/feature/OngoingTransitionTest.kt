package com.badoo.ribs.routing.state.feature

import com.badoo.ribs.routing.state.feature.RoutingStatePool.Effect.Transition.TransitionFinished
import com.badoo.ribs.routing.state.feature.RoutingStatePool.Effect.Transition.TransitionStarted
import com.badoo.ribs.routing.transition.TransitionElement
import com.badoo.ribs.test.TestConfiguration
import com.nhaarman.mockitokotlin2.*
import org.junit.Test

class OngoingTransitionTest {

    private val effectEmitter: EffectEmitter<TestConfiguration> = mock()
    private val transitionElement: TransitionElement<TestConfiguration> = mock()

    private val ongoingTransition = OngoingTransition(
        descriptor = mock(),
        actions = emptyList(),
        direction = mock(),
        transitionPair = mock(),
        transitionElements = listOf(transitionElement),
        emitter = effectEmitter,
    )

    @Test
    fun `GIVEN that there are pending transition elements WHEN transition is started THEN TransitionStarted is emitted but not TransitionFinished`() {
        whenever(transitionElement.isPending()).thenReturn(true)

        ongoingTransition.start()

        verify(effectEmitter).invoke(any<TransitionStarted<TestConfiguration>>())
        verify(effectEmitter, never()).invoke(any<TransitionFinished<TestConfiguration>>())

    }

    @Test
    fun `GIVEN that there are NOT pending transition elements WHEN transition has started THEN TransitionFinished is emitted`() {
        whenever(transitionElement.isPending()).thenReturn(false)

        ongoingTransition.start()

        verify(effectEmitter).invoke(any<TransitionFinished<TestConfiguration>>())
    }

    @Test
    fun `WHEN transition is jumpToEnd THEN TransitionFinished is emitted`() {
        ongoingTransition.jumpToEnd()

        verify(effectEmitter).invoke(any<TransitionFinished<TestConfiguration>>())
    }
}
