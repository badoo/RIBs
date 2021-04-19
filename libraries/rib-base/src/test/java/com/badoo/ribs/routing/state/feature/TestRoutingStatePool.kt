package com.badoo.ribs.routing.state.feature

import com.badoo.ribs.core.state.TimeCapsule
import com.badoo.ribs.routing.state.feature.state.SavedState
import com.badoo.ribs.routing.state.feature.state.WorkingState
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock

internal class TestRoutingStatePool(initialState: WorkingState<TestConfiguration>? = null) : RoutingStatePool<TestConfiguration>(
    timeCapsule = createTimeCapsule(initialState),
    resolver = mock(),
    activator = mock(),
    parentNode = mock(),
    transitionHandler = mock()
) {

    fun testEvent(event: Effect<TestConfiguration>) {
        emitEvent(event)
    }

    companion object {

        private fun createTimeCapsule(initialState: WorkingState<TestConfiguration>?): TimeCapsule =
            if (initialState == null) {
                TimeCapsule(null)
            } else {
                val savedState = mock<SavedState<TestConfiguration>> {
                    on { toWorkingState() } doReturn initialState
                }

                mock {
                    on { get<SavedState<TestConfiguration>>(any()) } doReturn savedState
                }
            }
    }
}
