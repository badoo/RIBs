package com.badoo.ribs.routing.state.feature

import com.badoo.ribs.core.helper.AnyConfiguration
import com.badoo.ribs.routing.state.feature.RoutingStatePool.Effect.Transition.TransitionFinished
import com.badoo.ribs.routing.state.feature.RoutingStatePool.Effect.Transition.TransitionStarted
import com.badoo.ribs.routing.transition.TransitionElement
import com.nhaarman.mockitokotlin2.*
import org.junit.Test

class OngoingTransitionTest {

    private val effectEmitter: EffectEmitter<AnyConfiguration> = mock()
    private val transitionElement: TransitionElement<AnyConfiguration> = mock()

    private val ongoingTransition = OngoingTransition(
        descriptor = mock(),
        actions = emptyList(),
        direction = mock(),
        transitionPair = mock(),
        transitionElements = listOf(transitionElement),
        emitter = effectEmitter,
        handler = mock()
    )

    @Test
    fun `GIVEN that there are pending transition elements WHEN transition is started THEN TransitionStarted is emitted but not TransitionFinished`() {
        whenever(transitionElement.isPending()).thenReturn(true)

        ongoingTransition.start()

        verify(effectEmitter).invoke(any<TransitionStarted<AnyConfiguration>>())
        verify(effectEmitter, never()).invoke(any<TransitionFinished<AnyConfiguration>>())

    }

    @Test
    fun `GIVEN that there are NOT pending transition elements WHEN transition has started THEN TransitionFinished is emitted`() {
        whenever(transitionElement.isPending()).thenReturn(false)

        ongoingTransition.start()

        verify(effectEmitter).invoke(any<TransitionFinished<AnyConfiguration>>())
    }

    @Test
    fun `WHEN transition is jumpToEnd THEN TransitionFinished is emitted`() {
        ongoingTransition.jumpToEnd()

        verify(effectEmitter).invoke(any<TransitionFinished<AnyConfiguration>>())
    }
}
