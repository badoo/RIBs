package com.badoo.ribs.routing.state.feature

import com.badoo.ribs.core.helper.AnyConfiguration
import com.badoo.ribs.routing.state.feature.state.WorkingState
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class RoutingStatePoolTest {

    @Test
    fun `WHEN cancel is called THEN ongoingTransition are disposed`() {
        val ongoingTransition: OngoingTransition<AnyConfiguration> = mock()
        val state = WorkingState(
            ongoingTransitions = listOf(ongoingTransition)
        )
        TestRoutingStatePool(state).cancel()

        verify(ongoingTransition).dispose()
    }

    @Test
    fun `WHEN cancel is called THEN pendingTransitions are discarded`() {
        val pendingTransition: PendingTransition<AnyConfiguration> = mock()
        val state = WorkingState(
            pendingTransitions = listOf(pendingTransition)
        )

        TestRoutingStatePool(state).cancel()

        verify(pendingTransition).cancel()
    }

    @Test
    fun `WHEN RequestTransition is emitted THEN a pendingTransitions is added to the state`() {
        val pendingTransition: PendingTransition<AnyConfiguration> = mock()
        val routingStatePool = TestRoutingStatePool()

        routingStatePool.testEvent(RoutingStatePool.Effect.Transition.RequestTransition(pendingTransition))

        assertThat(routingStatePool.state.pendingTransitions.first()).isEqualTo(pendingTransition)
    }

    @Test
    fun `GIVEN some pendingTransition in the state WHEN RemovePendingTransition is emitted THEN a pendingTransition is removed from the state`() {
        val pendingTransition: PendingTransition<AnyConfiguration> = mock()
        val state = WorkingState(
            pendingTransitions = listOf(pendingTransition)
        )
        val routingStatePool = TestRoutingStatePool(state)

        routingStatePool.testEvent(RoutingStatePool.Effect.Transition.RemovePendingTransition(pendingTransition))

        assertThat(routingStatePool.state.pendingTransitions).isEmpty()
    }

    @Test
    fun `WHEN TransitionStarted is emitted THEN an ongoingTransition is added to the state`() {
        val ongoingTransition: OngoingTransition<AnyConfiguration> = mock()
        val routingStatePool = TestRoutingStatePool()

        routingStatePool.testEvent(RoutingStatePool.Effect.Transition.TransitionStarted(ongoingTransition))

        assertThat(routingStatePool.state.ongoingTransitions.first()).isEqualTo(ongoingTransition)
    }

    @Test
    fun `WHEN TransitionFinished is emitted THEN a ongoingTransition is removed from the state`() {
        val ongoingTransition: OngoingTransition<AnyConfiguration> = mock()
        val state = WorkingState(
            ongoingTransitions = listOf(ongoingTransition)
        )
        val routingStatePool = TestRoutingStatePool(state)

        routingStatePool.testEvent(RoutingStatePool.Effect.Transition.TransitionFinished(ongoingTransition))

        assertThat(routingStatePool.state.ongoingTransitions).isEmpty()
    }

    @Test
    fun `GIVEN some pendingTransition in the state WHEN a new RequestTransition is emitted and remove pendingTransition with old value is emitten THEN a oldpendingTransition is removed  and new one is added to the state`() {
        val pendingTransition: PendingTransition<AnyConfiguration> = mock()
        val pendingTransition2: PendingTransition<AnyConfiguration> = mock()
        val state = WorkingState(
            pendingTransitions = listOf(pendingTransition)
        )
        val routingStatePool = TestRoutingStatePool(state)

        routingStatePool.testEvent(RoutingStatePool.Effect.Transition.RequestTransition(pendingTransition2))
        routingStatePool.testEvent(RoutingStatePool.Effect.Transition.RemovePendingTransition(pendingTransition))

        assertThat(routingStatePool.state.pendingTransitions).contains(pendingTransition2)
        assertThat(routingStatePool.state.pendingTransitions).doesNotContain(pendingTransition)
    }
}

