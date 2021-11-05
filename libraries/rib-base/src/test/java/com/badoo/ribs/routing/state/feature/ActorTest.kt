package com.badoo.ribs.routing.state.feature

import android.os.Parcelable
import com.badoo.ribs.core.helper.AnyConfiguration
import com.badoo.ribs.routing.state.RoutingContext
import com.badoo.ribs.routing.state.RoutingContext.ActivationState.ACTIVE
import com.badoo.ribs.routing.state.RoutingContext.ActivationState.SLEEPING
import com.badoo.ribs.routing.state.action.ActionExecutionParams
import com.badoo.ribs.routing.state.action.single.ReversibleAction
import com.badoo.ribs.routing.state.action.single.ReversibleActionFactory
import com.badoo.ribs.routing.state.changeset.RoutingCommand
import com.badoo.ribs.routing.state.changeset.TransitionDescriptor
import com.badoo.ribs.routing.state.feature.state.WorkingState
import com.badoo.ribs.routing.transition.TransitionElement
import com.badoo.ribs.routing.transition.handler.TransitionHandler
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.never
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import org.mockito.internal.verification.VerificationModeFactory.times
import java.util.stream.Stream

internal class ActorTest {

    @ParameterizedTest
    @MethodSource("scheduleTestArguments")
    fun `GIVEN no pending or ongoing transitions WHEN a RoutingChange is accepted THEN pendingTransition is correctly scheduled`(
        activationState: RoutingContext.ActivationState,
        transitionHandler: TransitionHandler<AnyConfiguration>?,
        isScheduled: Boolean
    ) {
        val pendingTransition: PendingTransition<AnyConfiguration> = mock()
        val pendingTransitionFactory: PendingTransitionFactory<AnyConfiguration> = mock {
            on { create(any(), any(), any(), any()) } doReturn pendingTransition
        }
        val state = WorkingState<AnyConfiguration>(activationLevel = activationState)
        val command = mock<RoutingCommand<AnyConfiguration>> {
            on { actionFactory } doReturn ElementsActionFactoryStub()
            on { routing } doReturn mock()
        }
        val transaction = Transaction.RoutingChange<AnyConfiguration>(
            changeset = listOf(command),
            descriptor = mock()
        )
        getActor(pendingTransitionFactory, transitionHandler).invoke(state, transaction)

        verify(pendingTransition, verifyMode(isScheduled)).schedule()
    }

    @ParameterizedTest
    @MethodSource("pendingInteractionsTestArguments")
    fun `GIVEN transition enabled conditions and a pendingTransition already in the state WHEN a RoutingChange is accepted THEN pendingTransition is correctly handled and new transition is scheduled `(
        isContinuation: Boolean,
        isReverse: Boolean
    ) {
        val pendingTransition: PendingTransition<AnyConfiguration> = mock()
        val pendingTransitionFactory: PendingTransitionFactory<AnyConfiguration> = mock {
            on { create(any(), any(), any(), any()) } doReturn pendingTransition
        }
        val oldPendingTransition: PendingTransition<AnyConfiguration> = mock {
            on { descriptor } doReturn mock()
        }
        val state = WorkingState(
            activationLevel = ACTIVE,
            pendingTransitions = listOf(oldPendingTransition)
        )
        val newDescriptor: TransitionDescriptor = mock {
            on { isReverseOf(oldPendingTransition.descriptor) } doReturn isReverse
            on { isContinuationOf(oldPendingTransition.descriptor) } doReturn isContinuation
        }
        val command = mock<RoutingCommand<AnyConfiguration>> {
            on { actionFactory } doReturn ElementsActionFactoryStub()
            on { routing } doReturn mock()
        }
        val transaction = Transaction.RoutingChange<AnyConfiguration>(
            changeset = listOf(command),
            descriptor = newDescriptor
        )

        getActor(pendingTransitionFactory, mock()).invoke(state, transaction)

        verify(oldPendingTransition, verifyMode(isContinuation)).completeWithoutTransition()
        verify(oldPendingTransition, verifyMode(isReverse)).discard()
        verify(pendingTransition).schedule()
    }

    @ParameterizedTest()
    @MethodSource("executePendingTestArguments")
    fun `WHEN a ExecutePendingTransition is accepted THEN pendingTransition is correctly handled`(
        activationState: RoutingContext.ActivationState,
        transitionHandler: TransitionHandler<AnyConfiguration>?,
        existingPendingTransition: PendingTransition<AnyConfiguration>,
        toExecuteTransition: PendingTransition<AnyConfiguration>,
        isCompletedWithoutTransition: Boolean,
        isDiscarded: Boolean,
        isExecuted: Boolean
    ) {
        val ongoingTransition: OngoingTransition<AnyConfiguration> = mock()
        whenever(toExecuteTransition.execute(any())).thenReturn(ongoingTransition)
        val state = WorkingState(
            activationLevel = activationState,
            pendingTransitions = listOf(existingPendingTransition)
        )
        getActor(mock(), transitionHandler).invoke(
            state,
            Transaction.InternalTransaction.ExecutePendingTransition(toExecuteTransition)
        )

        verify(
            toExecuteTransition,
            verifyMode(isCompletedWithoutTransition)
        ).completeWithoutTransition()
        verify(toExecuteTransition, verifyMode(isDiscarded)).discard()
        transitionHandler?.let {
            verify(toExecuteTransition, verifyMode(isExecuted)).execute(transitionHandler)
        }
    }

    @ParameterizedTest()
    @MethodSource("ongoingTransitionInteractionsTestArguments")
    fun `GIVEN an ongoingTransition in the state WHEN a RoutingChange is accepted THEN existing ongoingTransition is correctly handled`(
        isContinuation: Boolean,
        isReverse: Boolean
    ) {
        val pendingTransition: PendingTransition<AnyConfiguration> = mock()
        val pendingTransitionFactory: PendingTransitionFactory<AnyConfiguration> = mock {
            on { create(any(), any(), any(), any()) } doReturn pendingTransition
        }
        val ongoingTransition: OngoingTransition<AnyConfiguration> = mock {
            on { descriptor } doReturn mock()
        }
        val state = WorkingState(
            activationLevel = ACTIVE,
            ongoingTransitions = listOf(ongoingTransition)
        )
        val newDescriptor: TransitionDescriptor = mock {
            on { isReverseOf(ongoingTransition.descriptor) } doReturn isReverse
            on { isContinuationOf(ongoingTransition.descriptor) } doReturn isContinuation

        }

        val transaction = Transaction.RoutingChange<AnyConfiguration>(
            changeset = emptyList(),
            descriptor = newDescriptor
        )

        getActor(pendingTransitionFactory, mock()).invoke(state, transaction)

        verify(ongoingTransition, verifyMode(isReverse)).reverse()
        verify(ongoingTransition, verifyMode(isContinuation)).jumpToEnd()
    }


    @Test
    fun `GIVEN empty transitionElements WHEN processing a routing change THEN transition is not scheduled`() {

        val pendingTransition: PendingTransition<AnyConfiguration> = mock()
        val pendingTransitionFactory: PendingTransitionFactory<AnyConfiguration> = mock {
            on { create(any(), any(), any(), any()) } doReturn pendingTransition
        }
        val command = mock<RoutingCommand<AnyConfiguration>> {
            on { actionFactory } doReturn NoElementsActionFactoryStub()
            on { routing } doReturn mock()
        }
        val state = WorkingState<AnyConfiguration>(activationLevel = ACTIVE)
        val transaction = Transaction.RoutingChange(
            changeset = listOf(command),
            descriptor = mock()
        )
        getActor(pendingTransitionFactory, mock()).invoke(state, transaction)


        verify(pendingTransition, never()).schedule()
    }


    private fun getActor(
        pendingTransitionFactory: PendingTransitionFactory<AnyConfiguration>,
        transitionHandler: TransitionHandler<AnyConfiguration>? = mock()
    ) =
        Actor(
            resolver = mock(),
            activator = mock(),
            parentNode = mock(),
            transitionHandler = transitionHandler,
            effectEmitter = mock(),
            pendingTransitionFactory = pendingTransitionFactory
        )

    private fun verifyMode(isExpected: Boolean) =
        if (isExpected) times(1) else never()

    companion object {

        @JvmStatic
        fun scheduleTestArguments(): Stream<Arguments> = Stream.of(
            createScheduleTestArguments(
                activationState = ACTIVE,
                transitionHandler = mock(),
                isScheduled = true
            ),
            createScheduleTestArguments(
                activationState = SLEEPING,
                transitionHandler = mock(),
                isScheduled = false
            ),
            createScheduleTestArguments(
                activationState = ACTIVE,
                transitionHandler = null,
                isScheduled = false
            ),
            createScheduleTestArguments(
                activationState = SLEEPING,
                transitionHandler = null,
                isScheduled = false
            ),
        )

        private fun createScheduleTestArguments(
            activationState: RoutingContext.ActivationState,
            transitionHandler: TransitionHandler<AnyConfiguration>?,
            isScheduled: Boolean
        ) =
            Arguments.of(activationState, transitionHandler, isScheduled)

        @JvmStatic
        fun pendingInteractionsTestArguments(): Stream<Arguments> = Stream.of(
            interactionTestArguments(
                isContinuation = false,
                isReversed = true
            ),
            interactionTestArguments(
                isContinuation = true,
                isReversed = false
            ),
            interactionTestArguments(
                isContinuation = false,
                isReversed = false
            )
        )

        private fun interactionTestArguments(isContinuation: Boolean, isReversed: Boolean) =
            Arguments.of(isContinuation, isReversed)

        @JvmStatic
        fun executePendingTestArguments() =
            Stream.of(
                createExecutePendingTestArguments(
                    activationState = ACTIVE,
                    transitionHandler = mock(),
                    isNewSameAsExistingTransition = true,
                    isCompletedWithoutTransition = false,
                    isDiscarded = false,
                    isExecuted = true
                ),
                createExecutePendingTestArguments(
                    activationState = ACTIVE,
                    transitionHandler = mock(),
                    isNewSameAsExistingTransition = false,
                    isCompletedWithoutTransition = false,
                    isDiscarded = true,
                    isExecuted = false
                ),
                createExecutePendingTestArguments(
                    activationState = SLEEPING,
                    transitionHandler = mock(),
                    isNewSameAsExistingTransition = true,
                    isCompletedWithoutTransition = true,
                    isDiscarded = false,
                    isExecuted = false
                ),
                createExecutePendingTestArguments(
                    activationState = ACTIVE,
                    transitionHandler = null,
                    isNewSameAsExistingTransition = true,
                    isCompletedWithoutTransition = true,
                    isDiscarded = false,
                    isExecuted = false
                )

            )


        private fun createExecutePendingTestArguments(
            activationState: RoutingContext.ActivationState,
            transitionHandler: TransitionHandler<AnyConfiguration>?,
            isNewSameAsExistingTransition: Boolean,
            isCompletedWithoutTransition: Boolean,
            isDiscarded: Boolean,
            isExecuted: Boolean
        ): Arguments {

            val existingPendingTransition: PendingTransition<AnyConfiguration> = mock()
            val toExecuteTransition =
                if (isNewSameAsExistingTransition) existingPendingTransition else mock()

            return Arguments.of(
                activationState,
                transitionHandler,
                existingPendingTransition,
                toExecuteTransition,
                isCompletedWithoutTransition,
                isDiscarded,
                isExecuted
            )
        }

        @JvmStatic
        fun ongoingTransitionInteractionsTestArguments() =
            Stream.of(
                interactionTestArguments(isContinuation = false, isReversed = false),
                interactionTestArguments(isContinuation = true, isReversed = false),
                interactionTestArguments(isContinuation = false, isReversed = false),
            )
    }

    @Suppress("EmptyFunctionBlock")
    private class NoElementsActionFactoryStub : ReversibleActionFactory {

        override fun <C : Parcelable> create(params: ActionExecutionParams<C>): ReversibleAction<C> {
            return object : ReversibleAction<C> {
                override fun reverse() {}

                override fun onBeforeTransition() {}

                override fun onTransition(forceExecute: Boolean) {}

                override fun onFinish(forceExecute: Boolean) {}

                override val canExecute: Boolean
                    get() = true
                override val transitionElements: List<TransitionElement<C>>
                    get() = emptyList()
            }
        }
    }

    @Suppress("EmptyFunctionBlock")
    private class ElementsActionFactoryStub : ReversibleActionFactory {

        override fun <C : Parcelable> create(params: ActionExecutionParams<C>): ReversibleAction<C> {
            return object : ReversibleAction<C> {
                override fun reverse() {}

                override fun onBeforeTransition() {}

                override fun onTransition(forceExecute: Boolean) {}

                override fun onFinish(forceExecute: Boolean) {}

                override val canExecute: Boolean
                    get() = true
                override val transitionElements: List<TransitionElement<C>>
                    get() = listOf(mock())
            }
        }
    }
}
