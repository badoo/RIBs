package com.badoo.ribs.core.lifecycle

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Lifecycle.State.CREATED
import androidx.lifecycle.Lifecycle.State.DESTROYED
import androidx.lifecycle.Lifecycle.State.INITIALIZED
import androidx.lifecycle.Lifecycle.State.RESUMED
import androidx.lifecycle.Lifecycle.State.STARTED
import androidx.lifecycle.LifecycleRegistry
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import org.junit.Assert.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class CappedLifecycleTest {

    internal open lateinit var cappedLifecycle: CappedLifecycle

    private val lifecycleRegistry = mock<LifecycleRegistry>()

    @BeforeEach
    fun setUp() {
        cappedLifecycle = CappedLifecycle(lifecycleRegistry)
    }

    private fun setExternal(state: Lifecycle.State) {
        whenever(lifecycleRegistry.currentState).thenReturn(state)
        cappedLifecycle.update()
    }

    // region external: INITIALIZED

    @Test
    fun `when external is INITIALIZED, internal is after onCreate(), effective is CREATED`() {
        setExternal(INITIALIZED)
        cappedLifecycle.onCreate()
        assertEquals(CREATED, cappedLifecycle.lifecycle.currentState)
    }

    @Test
    fun `when external is INITIALIZED, internal is after onStart(), effective is CREATED`() {
        setExternal(INITIALIZED)
        cappedLifecycle.onStart()
        assertEquals(CREATED, cappedLifecycle.lifecycle.currentState)
    }

    @Test
    fun `when external is INITIALIZED, internal is after onResume(), effective is CREATED`() {
        setExternal(INITIALIZED)
        cappedLifecycle.onResume()
        assertEquals(CREATED, cappedLifecycle.lifecycle.currentState)
    }

    @Test
    fun `when external is INITIALIZED, internal is after onPause(), effective is CREATED`() {
        setExternal(INITIALIZED)
        cappedLifecycle.onPause()
        assertEquals(CREATED, cappedLifecycle.lifecycle.currentState)
    }

    @Test
    fun `when external is INITIALIZED, internal is after onStop(), effective is CREATED`() {
        setExternal(INITIALIZED)
        cappedLifecycle.onStop()
        assertEquals(CREATED, cappedLifecycle.lifecycle.currentState)
    }

    @Test
    fun `when external is INITIALIZED, internal is after onDestroy(), effective is DESTROYED`() {
        setExternal(INITIALIZED)
        cappedLifecycle.onDestroy()
        assertEquals(DESTROYED, cappedLifecycle.lifecycle.currentState)
    }

    // endregion

    // region external: CREATED

    @Test
    fun `when external is CREATED, internal is after onCreate(), effective is CREATED`() {
        setExternal(CREATED)
        cappedLifecycle.onCreate()
        assertEquals(CREATED, cappedLifecycle.lifecycle.currentState)
    }

    @Test
    fun `when external is CREATED, internal is after onStart(), effective is CREATED`() {
        setExternal(CREATED)
        cappedLifecycle.onStart()
        assertEquals(CREATED, cappedLifecycle.lifecycle.currentState)
    }

    @Test
    fun `when external is CREATED, internal is after onResume(), effective is CREATED`() {
        setExternal(CREATED)
        cappedLifecycle.onResume()
        assertEquals(CREATED, cappedLifecycle.lifecycle.currentState)
    }

    @Test
    fun `when external is CREATED, internal is after onPause(), effective is CREATED`() {
        setExternal(CREATED)
        cappedLifecycle.onPause()
        assertEquals(CREATED, cappedLifecycle.lifecycle.currentState)
    }

    @Test
    fun `when external is CREATED, internal is after onStop(), effective is CREATED`() {
        setExternal(CREATED)
        cappedLifecycle.onStop()
        assertEquals(CREATED, cappedLifecycle.lifecycle.currentState)
    }

    @Test
    fun `when external is CREATED, internal is after onDestroy(), effective is DESTROYED`() {
        setExternal(CREATED)
        cappedLifecycle.onDestroy()
        assertEquals(DESTROYED, cappedLifecycle.lifecycle.currentState)
    }

    // endregion

    // region external: STARTED

    @Test
    fun `when external is STARTED, internal is after onCreate(), effective is CREATED`() {
        setExternal(STARTED)
        cappedLifecycle.onCreate()
        assertEquals(CREATED, cappedLifecycle.lifecycle.currentState)
    }

    @Test
    fun `when external is STARTED, internal is after onStart(), effective is STARTED`() {
        setExternal(STARTED)
        cappedLifecycle.onStart()
        assertEquals(STARTED, cappedLifecycle.lifecycle.currentState)
    }

    @Test
    fun `when external is STARTED, internal is after onResume(), effective is STARTED`() {
        setExternal(STARTED)
        cappedLifecycle.onResume()
        assertEquals(STARTED, cappedLifecycle.lifecycle.currentState)
    }

    @Test
    fun `when external is STARTED, internal is after onPause(), effective is STARTED`() {
        setExternal(STARTED)
        cappedLifecycle.onPause()
        assertEquals(STARTED, cappedLifecycle.lifecycle.currentState)
    }

    @Test
    fun `when external is STARTED, internal is after onStop(), effective is CREATED`() {
        setExternal(STARTED)
        cappedLifecycle.onStop()
        assertEquals(CREATED, cappedLifecycle.lifecycle.currentState)
    }

    @Test
    fun `when external is STARTED, internal is after onDestroy(), effective is DESTROYED`() {
        setExternal(STARTED)
        cappedLifecycle.onDestroy()
        assertEquals(DESTROYED, cappedLifecycle.lifecycle.currentState)
    }

    // endregion

    // region external: RESUMED

    @Test
    fun `when external is RESUMED, internal is after onCreate(), effective is CREATED`() {
        setExternal(STARTED)
        cappedLifecycle.onCreate()
        assertEquals(CREATED, cappedLifecycle.lifecycle.currentState)
    }

    @Test
    fun `when external is RESUMED, internal is after onStart(), effective is STARTED`() {
        setExternal(STARTED)
        cappedLifecycle.onStart()
        assertEquals(STARTED, cappedLifecycle.lifecycle.currentState)
    }

    @Test
    fun `when external is RESUMED, internal is after onResume(), effective is RESUMED`() {
        setExternal(RESUMED)
        cappedLifecycle.onResume()
        assertEquals(RESUMED, cappedLifecycle.lifecycle.currentState)
    }

    @Test
    fun `when external is RESUMED, internal is after onPause(), effective is STARTED`() {
        setExternal(RESUMED)
        cappedLifecycle.onPause()
        assertEquals(STARTED, cappedLifecycle.lifecycle.currentState)
    }

    @Test
    fun `when external is RESUMED, internal is after onStop(), effective is CREATED`() {
        setExternal(RESUMED)
        cappedLifecycle.onStop()
        assertEquals(CREATED, cappedLifecycle.lifecycle.currentState)
    }

    @Test
    fun `when external is RESUMED, internal is after onDestroy(), effective is DESTROYED`() {
        setExternal(RESUMED)
        cappedLifecycle.onDestroy()
        assertEquals(DESTROYED, cappedLifecycle.lifecycle.currentState)
    }

    // endregion

    // region external: RESUMED

    @Test
    fun `when external is DESTROYED, internal is after onCreate(), effective is DESTROYED`() {
        setExternal(DESTROYED)
        cappedLifecycle.onCreate()
        assertEquals(DESTROYED, cappedLifecycle.lifecycle.currentState)
    }

    @Test
    fun `when external is DESTROYED, internal is after onStart(), effective is DESTROYED`() {
        setExternal(DESTROYED)
        cappedLifecycle.onStart()
        assertEquals(DESTROYED, cappedLifecycle.lifecycle.currentState)
    }

    @Test
    fun `when external is DESTROYED, internal is after onResume(), effective is DESTROYED`() {
        setExternal(DESTROYED)
        cappedLifecycle.onResume()
        assertEquals(DESTROYED, cappedLifecycle.lifecycle.currentState)
    }

    @Test
    fun `when external is DESTROYED, internal is after onPause(), effective is DESTROYED`() {
        setExternal(DESTROYED)
        cappedLifecycle.onPause()
        assertEquals(DESTROYED, cappedLifecycle.lifecycle.currentState)
    }

    @Test
    fun `when external is DESTROYED, internal is after onStop(), effective is DESTROYED`() {
        setExternal(DESTROYED)
        cappedLifecycle.onStop()
        assertEquals(DESTROYED, cappedLifecycle.lifecycle.currentState)
    }

    @Test
    fun `when external is DESTROYED, internal is after onDestroy(), effective is DESTROYED`() {
        setExternal(DESTROYED)
        cappedLifecycle.onDestroy()
        assertEquals(DESTROYED, cappedLifecycle.lifecycle.currentState)
    }

    // endregion
}
