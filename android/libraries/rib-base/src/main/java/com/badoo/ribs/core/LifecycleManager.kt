package com.badoo.ribs.core

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry

internal class LifecycleManager(
    private val owner: Node<*>
) : LifecycleOwner {

    internal open val externalLifecycle = LifecycleRegistry(this)
    internal open var ribLifecycle = CappedLifecycle(externalLifecycle)
    internal open var viewLifecycle: CappedLifecycle? = null

    override fun getLifecycle(): Lifecycle =
        ribLifecycle.lifecycle

    fun onAttachChild(child: Node<*>) {
        child.lifecycleManager.inheritExternalLifecycle(externalLifecycle)
    }

    private fun inheritExternalLifecycle(lifecycleRegistry: LifecycleRegistry) {
        externalLifecycle.markState(lifecycleRegistry.currentState)
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
     * This is intentionally not propagated to children, they are triggered by router
     */
    fun onCreateRib() {
        if (owner.isViewless) {
            // RIBs without view will always be at: min(RESUMED, external)
            ribLifecycle.onResume()
        } else {
            // RIBs with view be coupled to their view lifecycles
            ribLifecycle.onCreate()
        }
    }

    /**
     * This is intentionally not propagated to children, they are triggered by router
     */
    fun onDestroyRib() {
        ribLifecycle.onDestroy()
    }

    /**
     * This is intentionally not propagated to children, they are triggered by router
     */
    fun onCreateView() {
        viewLifecycle = CappedLifecycle(externalLifecycle)
        viewLifecycle!!.onCreate()

        if (!owner.isViewless) {
            // If isViewless, it's already RESUMED in onAttach()
            ribLifecycle.onStart()
            ribLifecycle.onResume()
        }

        viewLifecycle!!.onStart()
        viewLifecycle!!.onResume()
    }

    /**
     * This is intentionally not propagated to children, they are triggered by router
     */
    fun onDestroyView() {
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
