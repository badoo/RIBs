/*
 * Copyright (C) 2017. Uber Technologies
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.badoo.ribs.core

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.Lifecycle.Event.ON_CREATE
import android.arch.lifecycle.Lifecycle.Event.ON_DESTROY
import android.arch.lifecycle.Lifecycle.Event.ON_PAUSE
import android.arch.lifecycle.Lifecycle.Event.ON_RESUME
import android.arch.lifecycle.Lifecycle.Event.ON_START
import android.arch.lifecycle.Lifecycle.Event.ON_STOP
import android.arch.lifecycle.Lifecycle.State.CREATED
import android.arch.lifecycle.Lifecycle.State.INITIALIZED
import android.arch.lifecycle.Lifecycle.State.STARTED
import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.LifecycleRegistry
import android.os.Bundle
import android.os.Parcelable
import android.support.annotation.CallSuper
import android.support.annotation.MainThread
import android.util.SparseArray
import android.view.ViewGroup
import com.badoo.ribs.core.view.RibView
import com.badoo.ribs.core.view.ViewFactory
import com.badoo.ribs.util.RIBs
import com.uber.rib.util.RibRefWatcher
import java.util.concurrent.CopyOnWriteArrayList

/**
 * Responsible for handling the addition and removal of child nodes.
 **/
@SuppressWarnings("LargeClass")
open class Node<V : RibView>(
    internal open val identifier: Rib,
    private val viewFactory: ViewFactory<V>?,
    private val router: Router<*, *, *, *, V>,
    private val interactor: Interactor<*, *, *, V>,
    private val ribRefWatcher: RibRefWatcher = RibRefWatcher.getInstance()
) : LifecycleOwner {

    enum class ViewAttachMode {
        /**
         * The node's view attach/detach is managed by its parent.
         */
        PARENT,

        /**
         * The node's view is somewhere else in the view tree, and it should not be managed
         *  by its parent.
         *
         * Examples can be: the child's view is hosted in a dialog, or added to some other
         *  generic host node.
         */
        EXTERNAL
    }

    data class Descriptor(
        val node: Node<*>,
        val viewAttachMode: ViewAttachMode
    )

    companion object {
        internal const val KEY_ROUTER = "node.router"
        internal const val KEY_INTERACTOR = "node.interactor"
        internal const val KEY_VIEW_STATE = "view.state"
    }

    init {
        router.node = this
    }

    private val externalLifecycleRegistry = LifecycleRegistry(this)
    internal val ribLifecycleRegistry = LifecycleRegistry(this)
    internal val viewLifecycleRegistry = LifecycleRegistry(this)

    val tag: String = this::class.java.name
    internal val children = CopyOnWriteArrayList<Node<*>>()

    private val isViewless: Boolean =
        viewFactory == null

    internal open var view: V? = null
    protected var parentViewGroup: ViewGroup? = null

    private var savedInstanceState: Bundle? = null
    internal open var savedViewState: SparseArray<Parcelable> = SparseArray()

    internal var isAttachedToView: Boolean = false
        private set

    fun getChildren(): List<Node<*>> =
        children.toList()

    @CallSuper
    open fun onAttach(savedInstanceState: Bundle?) {
        this.savedInstanceState = savedInstanceState

        savedViewState = savedInstanceState?.getSparseParcelableArray<Parcelable>(KEY_VIEW_STATE) ?: SparseArray()

        if (externalLifecycleRegistry.currentState == INITIALIZED) externalLifecycleRegistry.handleLifecycleEvent(ON_CREATE)
        ribLifecycleRegistry.handleLifecycleEvent(ON_CREATE)
        router.onAttach(savedInstanceState?.getBundle(KEY_ROUTER))
        interactor.onAttach(savedInstanceState?.getBundle(KEY_INTERACTOR), ribLifecycleRegistry)
    }

    open fun attachToView(parentViewGroup: ViewGroup) {
        this.parentViewGroup = parentViewGroup
        isAttachedToView = true

        if (!isViewless) {
            view = createView(parentViewGroup)
            view!!.let {
                parentViewGroup.addView(it.androidView)
                it.androidView.restoreHierarchyState(savedViewState)
                viewLifecycleRegistry.handleLifecycleEvent(ON_CREATE)
                interactor.onViewCreated(viewLifecycleRegistry, it)
            }
        }

        router.onAttachView()
        onStartInternal()
        onResumeInternal()
    }

    private fun createView(parentViewGroup: ViewGroup): V? =
        viewFactory?.invoke(parentViewGroup)

    open fun detachFromView() {
        onPauseInternal()
        onStopInternal()
        router.onDetachView()

        if (!isViewless) {
            view!!.let {
                parentViewGroup!!.removeView(it.androidView)
                viewLifecycleRegistry.handleLifecycleEvent(ON_DESTROY)
            }
        }

        view = null
        isAttachedToView = false
        this.parentViewGroup = null
    }

    open fun onDetach() {
        if (isAttachedToView) {
            RIBs.errorHandler.handleNonFatalError(
                "View was not detached before node detach!",
                RuntimeException("View was not detached before node detach!")
            )
            detachFromView()
        }

        viewLifecycleRegistry.handleLifecycleEvent(ON_DESTROY)
        ribLifecycleRegistry.handleLifecycleEvent(ON_DESTROY)
        interactor.onDetach()
        router.onDetach()

        for (child in children) {
            detachChildNode(child)
        }
    }

    /**
     * Attaches a child node to this node.
     *
     * @param childNode the [Node] to be attached.
     */
    @MainThread
    internal fun attachChildNode(childNode: Node<*>, bundle: Bundle?) {
        children.add(childNode)
        ribRefWatcher.logBreadcrumb(
            "ATTACHED", childNode.javaClass.simpleName, this.javaClass.simpleName
        )

        childNode.inheritExternalLifecycle(externalLifecycleRegistry)
        childNode.onAttach(bundle)
    }

    private fun inheritExternalLifecycle(lifecycleRegistry: LifecycleRegistry) {
        externalLifecycleRegistry.markState(lifecycleRegistry.currentState)
    }

    internal fun attachChildView(child: Node<*>) {
        if (isAttachedToView) {
            val target = when {
                // parentViewGroup is guaranteed to be non-null if and only if view is attached
                isViewless -> parentViewGroup!!
                else -> view!!.getParentViewForChild(child.identifier) ?: parentViewGroup!!
            }

            child.attachToView(target)
        }
    }

    internal fun detachChildView(child: Node<*>) {
        parentViewGroup?.let {
            child.detachFromView()
        }
    }

    /**
     * Detaches the node from this parent. NOTE: No consumers of
     * this API should ever keep a reference to the detached child, leak canary will enforce
     * that it gets garbage collected.
     *
     * @param childNode the [Node] to be detached.
     */
    @MainThread
    internal fun detachChildNode(childNode: Node<*>) {
        children.remove(childNode)

        val interactor = childNode.interactor
        ribRefWatcher.watchDeletedObject(interactor)
        ribRefWatcher.logBreadcrumb(
            "DETACHED", childNode.javaClass.simpleName, this.javaClass.simpleName
        )

        childNode.onDetach()
    }

    /**
     * To be called only from the hosting environment (Activity, Fragment, etc.)
     *
     * For internal usage call onStartInternal() directly
     */
    fun onStart() {
        externalLifecycleRegistry.handleLifecycleEvent(ON_START)
        onStartInternal { it.onStart() }
    }

    /**
     * To be called only from the hosting environment (Activity, Fragment, etc.)
     *
     * For internal usage call onStopInternal() directly
     */
    fun onStop() {
        externalLifecycleRegistry.handleLifecycleEvent(ON_STOP)
        onStopInternal { it.onStop() }
    }

    /**
     * To be called only from the hosting environment (Activity, Fragment, etc.)
     *
     * For internal usage call onResumeInternal() directly
     */
    fun onResume() {
        externalLifecycleRegistry.handleLifecycleEvent(ON_RESUME)
        onResumeInternal { it.onResume() }
    }

    /**
     * To be called only from the hosting environment (Activity, Fragment, etc.)
     *
     * For internal usage call onPauseInternal() directly
     */
    fun onPause() {
        externalLifecycleRegistry.handleLifecycleEvent(ON_PAUSE)
        onPauseInternal { it.onPause() }
    }

    internal fun onStartInternal(callOnChildren: (Node<*>) -> Unit = { it.onStartInternal() }) {
        // The lifecycle cannot go higher than that of the hosting environment (e.g. Activity)
        updateToStateIfViewAttached(externalLifecycleRegistry.currentState)
        children.forEach { callOnChildren.invoke(it) }
    }

    // TODO call this when overlay is removed
    internal fun onResumeInternal(callOnChildren: (Node<*>) -> Unit = { it.onResumeInternal() }) {
        // The lifecycle cannot go higher than that of the hosting environment (e.g. Activity)
        updateToStateIfViewAttached(externalLifecycleRegistry.currentState)
        children.forEach { callOnChildren.invoke(it) }
    }

    // TODO call this when overlay hides content AND current rib is not in dialog
    internal fun onPauseInternal(callOnChildren: (Node<*>) -> Unit = { it.onPauseInternal() }) {
        // The lifecycle cannot go higher than that of the hosting environment (e.g. Activity)
        val targetState = if (externalLifecycleRegistry.currentState == CREATED) CREATED else STARTED
        updateToStateIfViewAttached(targetState)
        children.forEach { callOnChildren.invoke(it) }
    }

    internal fun onStopInternal(callOnChildren: (Node<*>) -> Unit = { it.onStopInternal() }) {
        updateToStateIfViewAttached(CREATED)
        children.forEach { callOnChildren.invoke(it) }
    }

    private fun updateToStateIfViewAttached(state: Lifecycle.State) {
        if (isAttachedToView) {
            ribLifecycleRegistry.markState(state)
            if (!isViewless) {
                view!!.let { viewLifecycleRegistry.markState(state) }
            }
        }
    }

    /**
     * Dispatch back press to the associated interactor.
     *
     * @return TRUE if the interactor handled the back press and no further action is necessary.
     */
    @CallSuper
    open fun handleBackPress(): Boolean {
        ribRefWatcher.logBreadcrumb("BACKPRESS", null, null)
        return children
            .filter { it.isAttachedToView }
            .any { it.handleBackPress() }
            || interactor.handleBackPress()
            || router.popBackStack()
    }


    internal fun saveViewState() {
        view?.let {
            it.androidView.saveHierarchyState(savedViewState)
        }
    }

    open fun onSaveInstanceState(outState: Bundle) {
        saveRouterState(outState)
        saveInteractorState(outState)
        saveViewState()
        outState.putSparseParcelableArray(KEY_VIEW_STATE, savedViewState)
    }

    private fun saveRouterState(outState: Bundle) {
        Bundle().let {
            router.onSaveInstanceState(it)
            outState.putBundle(KEY_ROUTER, it)
        }
    }

    private fun saveInteractorState(outState: Bundle) {
        Bundle().let {
            interactor.onSaveInstanceState(it)
            outState.putBundle(KEY_INTERACTOR, it)
        }
    }

    fun onLowMemory() {
        router.onLowMemory()
    }

    override fun getLifecycle(): Lifecycle =
        ribLifecycleRegistry

    override fun toString(): String =
        "Node@${hashCode()} ($identifier)"
}
