package com.badoo.ribs.routing.state.feature

import com.badoo.ribs.routing.state.RoutingContext
import com.badoo.ribs.routing.state.changeset.TransitionDescriptor
import com.badoo.ribs.routing.state.feature.state.WorkingState
import com.badoo.ribs.routing.transition.handler.TransitionHandler
import com.badoo.ribs.test.TestConfiguration
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.never
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import org.junit.Test

class ActorTest {

    private val pendingTransition: PendingTransition<TestConfiguration> = mock()
    private val pendingTransitionFactory: PendingTransitionFactory<TestConfiguration> = mock {
        on { create(any(), any(), any(), any()) } doReturn pendingTransition
    }

    /**
     * [Case-1]
     *  1. there is no ongoingTransitions
     *  2. there is no pendingTransitions
     *  3. globalActivationState is NOT SLEEPING
     *  4. transitionHandler is NOT null
     */
    @Test
    fun `GIVEN Case-1 conditions WHEN a RoutingChange is accepted THEN a pendingTransition is scheduled`() {
        val state = WorkingState<TestConfiguration>(activationLevel = RoutingContext.ActivationState.ACTIVE)
        val transaction = Transaction.RoutingChange<TestConfiguration>(
            changeset = emptyList(),
            descriptor = mock()
        )

        getActor().invoke(state, transaction)

        verify(pendingTransition).schedule()
    }

    /**
     * [Case-2]
     *  1. there is no ongoingTransitions
     *  2. there is no pendingTransitions
     *  2. globalActivationState is SLEEPING
     *  3. transitionHandler is NOT null
     */
    @Test
    fun `GIVEN Case-2 conditions WHEN a RoutingChange is accepted THEN a pendingTransition is NOT scheduled`() {
        val state = WorkingState<TestConfiguration>(activationLevel = RoutingContext.ActivationState.SLEEPING)
        val transaction = Transaction.RoutingChange<TestConfiguration>(
            changeset = emptyList(),
            descriptor = mock()
        )

        getActor().invoke(state, transaction)

        verify(pendingTransition, never()).schedule()
    }

    /**
     * [Case-3]
     *  1. there is no ongoingTransitions
     *  2. there is no pendingTransitions
     *  3. globalActivationState is NOT SLEEPING
     *  4. transitionHandler is null
     */
    @Test
    fun `GIVEN Case-3 conditions WHEN a RoutingChange is accepted THEN a pendingTransition is NOT scheduled`() {
        val state = WorkingState<TestConfiguration>(activationLevel = RoutingContext.ActivationState.ACTIVE)
        val transaction = Transaction.RoutingChange<TestConfiguration>(
            changeset = emptyList(),
            descriptor = mock()
        )

        getActor(null).invoke(state, transaction)

        verify(pendingTransition, never()).schedule()
    }

    /**
     * [Case-4]
     *  1. there is no ongoingTransitions
     *  2. there is 1 pendingTransitions
     *  3. globalActivationState is NOT SLEEPING
     *  4. transitionHandler is NOT null
     *  5. The existing pending transition is the reversed of the new RoutingChange
     */
    @Test
    fun `GIVEN Case-4 conditions WHEN a RoutingChange is accepted THEN a the old pending transition is discarded, and new one is scheduled`() {
        val oldPendingTransition: PendingTransition<TestConfiguration> = mock {
            on { descriptor } doReturn mock()
        }
        val state = WorkingState(
            activationLevel = RoutingContext.ActivationState.ACTIVE,
            pendingTransitions = listOf(oldPendingTransition)
        )
        val newDescriptor: TransitionDescriptor = mock {
            on { isReverseOf(oldPendingTransition.descriptor) } doReturn true
        }
        val transaction = Transaction.RoutingChange<TestConfiguration>(
            changeset = emptyList(),
            descriptor = newDescriptor
        )

        getActor().invoke(state, transaction)

        verify(oldPendingTransition).discard()
        verify(pendingTransition).schedule()
    }

    /**
     * [Case-5]
     *  1. there is no ongoingTransitions
     *  2. there is 1 pendingTransitions
     *  3. globalActivationState is NOT SLEEPING
     *  4. transitionHandler is NOT null
     *  5. The existing pending transition is the continuation of the new RoutingChange
     */
    @Test
    fun `GIVEN Case-5 conditions WHEN a RoutingChange is accepted THEN the old pending transition is completeWithoutTransition, and new one is scheduled`() {
        val oldPendingTransition: PendingTransition<TestConfiguration> = mock {
            on { descriptor } doReturn mock()
        }
        val state = WorkingState(
            activationLevel = RoutingContext.ActivationState.ACTIVE,
            pendingTransitions = listOf(oldPendingTransition)
        )
        val newDescriptor: TransitionDescriptor = mock {
            on { isReverseOf(oldPendingTransition.descriptor) } doReturn false
            on { isContinuationOf(oldPendingTransition.descriptor) } doReturn true
        }
        val transaction = Transaction.RoutingChange<TestConfiguration>(
            changeset = emptyList(),
            descriptor = newDescriptor
        )

        getActor().invoke(state, transaction)

        verify(oldPendingTransition).completeWithoutTransition()
        verify(pendingTransition).schedule()
    }

    /**
     * [Case-6]
     *  1. there is no ongoingTransitions
     *  2. there is no pendingTransitions
     *  3. globalActivationState is NOT SLEEPING
     *  4. transitionHandler is NOT null
     *  5. The existing pending transition is the continuation of the new RoutingChange
     */
    @Test
    fun `GIVEN Case-6 conditions WHEN a RoutingChange is accepted THEN the old pending transition is completeWithoutTransition, and new one is scheduled`() {
        val oldPendingTransition: PendingTransition<TestConfiguration> = mock {
            on { descriptor } doReturn mock()
        }
        val state = WorkingState(
            activationLevel = RoutingContext.ActivationState.ACTIVE,
            pendingTransitions = listOf(oldPendingTransition)
        )
        val newDescriptor: TransitionDescriptor = mock {
            on { isReverseOf(oldPendingTransition.descriptor) } doReturn false
            on { isContinuationOf(oldPendingTransition.descriptor) } doReturn true
        }
        val transaction = Transaction.RoutingChange<TestConfiguration>(
            changeset = emptyList(),
            descriptor = newDescriptor
        )

        getActor().invoke(state, transaction)

        verify(oldPendingTransition).completeWithoutTransition()
        verify(pendingTransition).schedule()
    }

    /**
     * [Case-7]
     *  1. there is no ongoingTransitions
     *  2. there is no pendingTransitions
     *  3. globalActivationState is NOT SLEEPING
     *  4. transitionHandler is NOT null
     *  5. The existing pending transition has no interactions with the new one
     */
    @Test
    fun `GIVEN Case-7 conditions WHEN a RoutingChange is accepted THEN then new one is scheduled and no actions are applied to old one`() {
        val oldPendingTransition: PendingTransition<TestConfiguration> = mock {
            on { descriptor } doReturn mock()
        }
        val state = WorkingState(
            activationLevel = RoutingContext.ActivationState.ACTIVE,
            pendingTransitions = listOf(oldPendingTransition)
        )
        val newDescriptor: TransitionDescriptor = mock {
            on { isReverseOf(oldPendingTransition.descriptor) } doReturn false
            on { isContinuationOf(oldPendingTransition.descriptor) } doReturn false
        }
        val transaction = Transaction.RoutingChange<TestConfiguration>(
            changeset = emptyList(),
            descriptor = newDescriptor
        )

        getActor().invoke(state, transaction)

        verify(oldPendingTransition, never()).completeWithoutTransition()
        verify(oldPendingTransition, never()).discard()
        verify(pendingTransition).schedule()
    }

    /**
     * [Case-8]
     *  1. globalActivationState is NOT SLEEPING
     *  2. there is 1 pendingTransitions
     *  3. transitionHandler is NOT null
     *  4. Accepted transition is the pending one.
     */
    @Test
    fun `GIVEN Case-8 conditions WHEN a ExecutePendingTransition is accepted THEN then the pendingTransition is executed and ongoingTransition started`() {
        val ongoingTransition: OngoingTransition<TestConfiguration> = mock()
        val state = WorkingState(
            activationLevel = RoutingContext.ActivationState.ACTIVE,
            pendingTransitions = listOf(pendingTransition)
        )
        whenever(pendingTransition.execute(any())).thenReturn(ongoingTransition)

        getActor().invoke(state, Transaction.InternalTransaction.ExecutePendingTransition(pendingTransition))

        verify(pendingTransition).execute(any())
        verify(ongoingTransition).start()
    }

    /**
     * [Case-9]
     *  1. globalActivationState is NOT SLEEPING
     *  2. there is 1 pendingTransitions
     *  3. transitionHandler is NOT null
     *  4. Accepted transition is NOT the pending one.
     */
    @Test
    fun `GIVEN Case-9 conditions WHEN a ExecutePendingTransition is accepted THEN then the pendingTransition is discarded`() {
        val state = WorkingState(
            activationLevel = RoutingContext.ActivationState.ACTIVE,
            pendingTransitions = listOf<PendingTransition<TestConfiguration>>(mock())
        )

        getActor().invoke(state, Transaction.InternalTransaction.ExecutePendingTransition(pendingTransition))

        verify(pendingTransition).discard()
    }

    /**
     * [Case-10]
     *  1. globalActivationState is SLEEPING
     *  2. there is 1 pendingTransitions
     *  3. transitionHandler is NOT null
     *  4. Accepted transition is the pending one.
     */
    @Test
    fun `GIVEN Case-10 conditions WHEN a ExecutePendingTransition is accepted THEN then the pendingTransition is completedWithoutTransition`() {
        val state = WorkingState(
            activationLevel = RoutingContext.ActivationState.SLEEPING,
            pendingTransitions = listOf(pendingTransition)
        )

        getActor().invoke(state, Transaction.InternalTransaction.ExecutePendingTransition(pendingTransition))

        verify(pendingTransition).completeWithoutTransition()
    }

    /**
     * [Case-11]
     *  1. globalActivationState is NOT SLEEPING
     *  2. there is 1 pendingTransitions
     *  3. transitionHandler is null
     *  4. Accepted transition is the pending one.
     */
    @Test
    fun `GIVEN Case-11 conditions WHEN a ExecutePendingTransition is accepted THEN then the pendingTransition is completedWithoutTransition`() {
        val state = WorkingState(
            activationLevel = RoutingContext.ActivationState.ACTIVE,
            pendingTransitions = listOf(pendingTransition)
        )

        getActor(null).invoke(state, Transaction.InternalTransaction.ExecutePendingTransition(pendingTransition))

        verify(pendingTransition).completeWithoutTransition()
    }

    /**
     * [Case-12]
     *  1. globalActivationState is NOT SLEEPING
     *  2. transitionHandler is NOT null
     *  3. there is no pendingTransitions
     *  4. there is 1 ongoingTransition
     *  4. The existing ongoingTransition is the reverse of the new one.
     */
    @Test
    fun `GIVEN Case-12 conditions WHEN a RoutingChange is accepted THEN then the old one is reversed, and new one is NOT scheduled`() {
        val ongoingTransition: OngoingTransition<TestConfiguration> = mock {
            on { descriptor } doReturn mock()
        }
        val state = WorkingState(
            activationLevel = RoutingContext.ActivationState.ACTIVE,
            ongoingTransitions = listOf(ongoingTransition)
        )
        val newDescriptor: TransitionDescriptor = mock {
            on { isReverseOf(ongoingTransition.descriptor) } doReturn true
        }

        val transaction = Transaction.RoutingChange<TestConfiguration>(
            changeset = emptyList(),
            descriptor = newDescriptor
        )

        getActor().invoke(state, transaction)

        verify(ongoingTransition).reverse()
        verify(pendingTransition, never()).schedule()
    }

    /**
     * [Case-13]
     *  1. globalActivationState is NOT SLEEPING
     *  2. transitionHandler is NOT null
     *  3. there is no pendingTransitions
     *  4. there is 1 ongoingTransition
     *  4. The new transition is the continuation of the ongoing one.
     */
    @Test
    fun `GIVEN Case-13 conditions WHEN a RoutingChange is accepted THEN then the old one is jumpToEnd, and new one is scheduled`() {
        val ongoingTransition: OngoingTransition<TestConfiguration> = mock {
            on { descriptor } doReturn mock()
        }
        val state = WorkingState(
            activationLevel = RoutingContext.ActivationState.ACTIVE,
            ongoingTransitions = listOf(ongoingTransition)
        )
        val newDescriptor: TransitionDescriptor = mock {
            on { isContinuationOf(ongoingTransition.descriptor) } doReturn true
        }

        val transaction = Transaction.RoutingChange<TestConfiguration>(
            changeset = emptyList(),
            descriptor = newDescriptor
        )

        getActor().invoke(state, transaction)

        verify(ongoingTransition).jumpToEnd()
        verify(pendingTransition).schedule()
    }

    /**
     * [Case-14]
     *  1. globalActivationState is NOT SLEEPING
     *  2. transitionHandler is NOT null
     *  3. there is no pendingTransitions
     *  4. there is 1 ongoingTransition
     *  4. The new transition has no interaction with ongoing one
     */
    @Test
    fun `GIVEN Case-14 conditions WHEN a RoutingChange is accepted THEN then new one is scheduled and no actions are applied to old one`() {
        val ongoingTransition: OngoingTransition<TestConfiguration> = mock {
            on { descriptor } doReturn mock()
        }
        val state = WorkingState(
            activationLevel = RoutingContext.ActivationState.ACTIVE,
            ongoingTransitions = listOf(ongoingTransition)
        )
        val newDescriptor: TransitionDescriptor = mock {
            on { isReverseOf(ongoingTransition.descriptor) } doReturn false
            on { isContinuationOf(ongoingTransition.descriptor) } doReturn false

        }

        val transaction = Transaction.RoutingChange<TestConfiguration>(
            changeset = emptyList(),
            descriptor = newDescriptor
        )

        getActor().invoke(state, transaction)

        verify(ongoingTransition, never()).reverse()
        verify(ongoingTransition, never()).jumpToEnd()
        verify(pendingTransition).schedule()
    }


    private fun getActor(transitionHandler: TransitionHandler<TestConfiguration>? = mock()) = Actor(
        resolver = mock(),
        activator = mock(),
        parentNode = mock(),
        transitionHandler = transitionHandler,
        effectEmitter = mock(),
        pendingTransitionFactory = pendingTransitionFactory
    )
}
