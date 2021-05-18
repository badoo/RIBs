package com.badoo.ribs.test.builder

import androidx.lifecycle.Lifecycle
import com.badoo.ribs.test.assertEquals

fun RibBuilderStub<*, *>.assertLastNodeLifecycleState(state: Lifecycle.State) {
    assertEquals(state, lastNode?.lifecycle?.currentState)
}

fun RibBuilderStub<*, *>.assertLastNodeResumed() {
    assertLastNodeLifecycleState(Lifecycle.State.RESUMED)
}

fun RibBuilderStub<*, *>.assertLastNodeStarted() {
    assertLastNodeLifecycleState(Lifecycle.State.STARTED)
}
