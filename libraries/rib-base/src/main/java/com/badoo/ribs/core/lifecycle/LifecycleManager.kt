package com.badoo.ribs.core.lifecycle

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import com.badoo.ribs.core.Node

internal class LifecycleManager(
    private val owner: Node<*>
) : LifecycleOwner {

    internal val externalLifecycle = LifecycleRegistry(this)
    internal var ribLifecycle = CappedLifecycle(externalLifecycle)
    internal var viewLifecycle: CappedLifecycle? = null

    override fun getLifecycle(): Lifecycle =
        ribLifecycle.lifecycle

    fun onAttachChild(child: Node<*>) {
        child.lifecycleManager.inheritExternalLifecycle(externalLifecycle)
    }

    private fun inheritExternalLifecycle(lifecycleRegistry: LifecycleRegistry) {
        externalLifecycle.markState(lifecycleRegistry.currentState)
        ribLifecycle.update()
        viewLifecycle?.update()

        owner.children.forEach {
            it.lifecycleManager.inheritExternalLifecycle(lifecycleRegistry)
        }
    }

    /**
     * To be called only from the hosting environment (Activity, Fragment, etc.)
     *
     * For internal usage call onStart() with proper inner lifecycle registry directly
     */
    fun onStartExternal() {
        externalLifecycle.handleLifecycleEvent(Lifecycle.Event.ON_START)
        ribLifecycle.update()
        viewLifecycle?.update()
        owner.children.forEach { it.onStart() }
    }

    /**
     * To be called only from the hosting environment (Activity, Fragment, etc.)
     *
     * For internal usage call onStop() with proper inner lifecycle registry directly
     */
    fun onStopExternal() {
        externalLifecycle.handleLifecycleEvent(Lifecycle.Event.ON_STOP)
        ribLifecycle.update()
        viewLifecycle?.update()
        owner.children.forEach { it.onStop() }
    }

    /**
     * To be called only from the hosting environment (Activity, Fragment, etc.)
     *
     * For internal usage call onResume() with proper inner lifecycle registry directly
     */
    fun onResumeExternal() {
        externalLifecycle.handleLifecycleEvent(Lifecycle.Event.ON_RESUME)
        ribLifecycle.update()
        viewLifecycle?.update()
        owner.children.forEach { it.onResume() }
    }

    /**
     * To be called only from the hosting environment (Activity, Fragment, etc.)
     *
     * For internal usage call onPause() with proper inner lifecycle registry directly
     */
    fun onPauseExternal() {
        externalLifecycle.handleLifecycleEvent(Lifecycle.Event.ON_PAUSE)
        ribLifecycle.update()
        viewLifecycle?.update()
        owner.children.forEach { it.onPause() }
    }

    /**
     * This is intentionally not propagated to children, they are triggered by routing mechanisms
     */
    fun onCreate() {
        if (owner.isViewless) {
            // RIBs without view will always be at: min(RESUMED, external)
            ribLifecycle.onResume()
        } else {
            // RIBs with view be coupled to their view lifecycles
            ribLifecycle.onCreate()
        }
    }

    /**
     * This is intentionally not propagated to children, they are triggered by routing mechanisms
     */
    fun onDestroy() {
        ribLifecycle.onDestroy()
    }

    fun onViewCreated() {
        viewLifecycle = CappedLifecycle(externalLifecycle)
        viewLifecycle!!.onCreate()
    }

    /**
     * This is intentionally not propagated to children, they are triggered by routing mechanisms
     */
    fun onAttachToView() {
        if (!owner.isViewless) {
            if (viewLifecycle == null) {
                error("Trying to attach, but view was not created")
            }
            // If isViewless, it's already RESUMED in onAttach()
            ribLifecycle.onStart()
            ribLifecycle.onResume()
            viewLifecycle!!.onStart()
            viewLifecycle!!.onResume()
        }
    }

    /**
     * This is intentionally not propagated to children, they are triggered by routing mechanisms
     */
    fun onDetachFromView() {
        if (owner.isViewless) {
            // Implication: if RIB is viewless, we're keeping it resumed capped by external lifecycle only
            return
        }

        viewLifecycle!!.let {
            it.onPause()
            it.onStop()
            it.onDestroy()
        }

        viewLifecycle = null

        ribLifecycle.let {
            it.onPause()
            it.onStop()
        }
    }
}
