package com.badoo.ribs.routing.state.feature

import android.os.Handler
import com.badoo.ribs.core.helper.AnyConfiguration
import com.badoo.ribs.routing.state.feature.RoutingStatePool.Effect.Transition.RemovePendingTransition
import com.badoo.ribs.routing.state.feature.RoutingStatePool.Effect.Transition.RequestTransition
import com.badoo.ribs.routing.state.feature.Transaction.InternalTransaction.ExecutePendingTransition
import com.badoo.ribs.routing.transition.handler.TransitionHandler
import com.nhaarman.mockitokotlin2.*
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test


class PendingTransitionTest {

    private val effectEmitter: EffectEmitter<AnyConfiguration> = mock()
    private val internalTransactionConsumer: InternalTransactionConsumer<AnyConfiguration> = mock()
    private val handler = mock<Handler> {
        on { post(any()) } doAnswer {
            (it.arguments[0] as Runnable).run()
            true
        }
    }

    private val pendingTransition = PendingTransition(
        descriptor = mock(),
        actions = mock(),
        direction = mock(),
        transitionElements = emptyList(),
        effectEmitter = effectEmitter,
        internalTransactionConsumer = internalTransactionConsumer,
        handler = handler
    )

    @Test
    fun `WHEN transition is scheduled THEN RequestTransition is emitted`() {
        pendingTransition.schedule()
        verify(effectEmitter).invoke(any<RequestTransition<AnyConfiguration>>())
    }

    @Test
    fun `WHEN transition is scheduled THEN ExecutePendingTransition transaction is consumed`() {
        pendingTransition.schedule()

        verify(internalTransactionConsumer).invoke(any<ExecutePendingTransition<AnyConfiguration>>())
    }

    @Test
    fun `WHEN transition is executed THEN ExecutePendingTransition transaction is discarded and ongoingTransition is returned`() {
        val transitionHandler: TransitionHandler<AnyConfiguration> = mock {
            on { onTransition(any()) } doReturn mock()
        }
        val ongoingTransition = pendingTransition.execute(transitionHandler)

        verify(effectEmitter).invoke(any<RemovePendingTransition<AnyConfiguration>>())
        assertThat(ongoingTransition.descriptor).isEqualTo(pendingTransition.descriptor)
    }
}
