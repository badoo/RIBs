package com.badoo.ribs.core.lifecycle

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Lifecycle.Event.ON_CREATE
import androidx.lifecycle.Lifecycle.Event.ON_DESTROY
import androidx.lifecycle.Lifecycle.Event.ON_PAUSE
import androidx.lifecycle.Lifecycle.Event.ON_RESUME
import androidx.lifecycle.Lifecycle.Event.ON_START
import androidx.lifecycle.Lifecycle.Event.ON_STOP
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry

/**
 * Applies a capping of lifecycles by making:
 *
 * effective = min(external, internal) followed by replace INITIALIZED => CREATED
 */
internal class CappedLifecycle(
    private val external: LifecycleRegistry
): LifecycleOwner {

    private val internal = LifecycleRegistry(this)
    private var effective = LifecycleRegistry(this)

    override fun getLifecycle(): Lifecycle =
        effective

    fun onCreate() {
        internal.handleLifecycleEvent(ON_CREATE)
        update()
    }

    fun onDestroy() {
        internal.handleLifecycleEvent(ON_DESTROY)
        update()
    }

    fun onStart() {
        internal.handleLifecycleEvent(ON_START)
        update()
    }

    fun onStop() {
        internal.handleLifecycleEvent(ON_STOP)
        update()
    }

    fun onResume() {
        internal.handleLifecycleEvent(ON_RESUME)
        update()
    }

    fun onPause() {
        internal.handleLifecycleEvent(ON_PAUSE)
        update()
    }

    /**
     * Needs to be called whenever internal or external lifecycles change to update effective lifecycle
     */
    internal fun update() {
        effective.markState(
            capLifecycle(external, internal)
        )
    }

    /**
     * Takes the minimum of the two lifecycles, and enforces it to at least CREATED level.
     * (We do not track INITIALIZED state)
     */
    private fun capLifecycle(reg1: LifecycleRegistry, reg2: LifecycleRegistry?): Lifecycle.State =
        replaceInitializedToCreated(
            min(reg1.currentState, reg2?.currentState)
        )

    private fun replaceInitializedToCreated(state: Lifecycle.State): Lifecycle.State =
        if (state == Lifecycle.State.INITIALIZED) Lifecycle.State.CREATED else state

    private fun min(state1: Lifecycle.State, state2: Lifecycle.State?): Lifecycle.State =
        if (state2 != null && state2 < state1) state2 else state1
}
