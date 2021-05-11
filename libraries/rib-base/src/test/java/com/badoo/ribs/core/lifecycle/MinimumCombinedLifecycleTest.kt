package com.badoo.ribs.core.lifecycle

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Lifecycle.State
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import org.junit.Assert.assertEquals
import org.junit.Test

class MinimumCombinedLifecycleTest {

    @Test
    fun `completely duplicates state of single lifecycle`() {
        val test = TestLifecycle()
        val combined = MinimumCombinedLifecycle(test.lifecycle)
        STATES.forEach {
            if (it != State.INITIALIZED) {
                test.state = it
            }
            assertEquals(it, combined.lifecycle.currentState)
        }
    }

    @Test
    fun `completely duplicates state of lifecycles with same state`() {
        val test1 = TestLifecycle()
        val test2 = TestLifecycle()
        val combined = MinimumCombinedLifecycle(test1.lifecycle, test2.lifecycle)
        STATES.forEach {
            if (it != State.INITIALIZED) {
                test1.state = it
                test2.state = it
            }
            assertEquals(it, combined.lifecycle.currentState)
        }
    }

    @Test
    fun `destroyed when one of lifecycles is destroyed`() {
        val test1 = TestLifecycle()
        test1.state = State.DESTROYED
        val test2 = TestLifecycle()
        val combined = MinimumCombinedLifecycle(test1.lifecycle, test2.lifecycle)
        assertEquals(State.DESTROYED, combined.lifecycle.currentState)
    }

    private class TestLifecycle : LifecycleOwner {
        private val registry = LifecycleRegistry(this)

        var state: State
            get() = registry.currentState
            set(value) {
                registry.currentState = value
            }

        override fun getLifecycle(): Lifecycle = registry

    }

    companion object {
        val STATES =
            listOf(State.INITIALIZED, State.CREATED, State.STARTED, State.RESUMED, State.DESTROYED)
    }

}
