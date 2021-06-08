package com.badoo.ribs.routing.state.feature

import com.badoo.ribs.core.helper.AnyConfiguration
import com.badoo.ribs.minimal.state.TimeCapsule
import com.badoo.ribs.routing.state.feature.state.SavedState
import com.badoo.ribs.routing.state.feature.state.WorkingState
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock

internal class TestRoutingStatePool(initialState: WorkingState<AnyConfiguration>? = null) : RoutingStatePool<AnyConfiguration>(
    timeCapsule = createTimeCapsule(initialState),
    resolver = mock(),
    activator = mock(),
    parentNode = mock(),
    transitionHandler = mock(),
) {

    fun testEvent(event: Effect<AnyConfiguration>) {
        emitEvent(event)
    }

    companion object {

        private fun createTimeCapsule(initialState: WorkingState<AnyConfiguration>?): TimeCapsule =
            if (initialState == null) {
                TimeCapsule(null)
            } else {
                val savedState = mock<SavedState<AnyConfiguration>> {
                    on { toWorkingState() } doReturn initialState
                }

                mock {
                    on { get<SavedState<AnyConfiguration>>(any()) } doReturn savedState
                }
            }
    }
}
