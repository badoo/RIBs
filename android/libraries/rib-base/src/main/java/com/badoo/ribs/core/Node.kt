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
import android.support.annotation.VisibleForTesting
import android.util.Log
import android.util.SparseArray
import android.view.ViewGroup
import com.badoo.ribs.core.view.RibView
import com.badoo.ribs.util.RIBs
import com.jakewharton.rxrelay2.BehaviorRelay
import com.jakewharton.rxrelay2.PublishRelay
import com.uber.rib.util.RibRefWatcher
import io.reactivex.Observable
import io.reactivex.Single
import java.lang.IllegalStateException
import java.util.concurrent.CopyOnWriteArrayList

/**
 * Responsible for handling the addition and removal of child nodes.
 **/
@SuppressWarnings("LargeClass")
open class Node<V : RibView>(
    savedInstanceState: Bundle?,
    internal open val identifier: Rib,
    private val viewFactory: ((ViewGroup) -> V?)?,
    private val router: Router<*, *, *, *, V>,
    private val interactor: Interactor<*, *, *, V>,
    private val ribRefWatcher: RibRefWatcher = RibRefWatcher.getInstance()
) : LifecycleOwner, LifecycleParent, LifecycleChild {

    override fun addParented(child: LifecycleChild) {
        Log.d("CHECK", "addParented - $this ---> $child")
        lifecycleChildren.add(child)
    }

    override fun removeParented(child: LifecycleChild) {
        Log.d("CHECK", "removeParented - $this -x-> $child")
        lifecycleChildren.remove(child)
    }

    private val savedInstanceState = savedInstanceState?.getBundle(BUNDLE_KEY)

    enum class AttachMode {
        /**
         * The node's view attach/detach and lifecycle is managed by its parent.
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
        val viewAttachMode: AttachMode
    )

    companion object {
        internal const val BUNDLE_KEY = "Node"
        internal const val KEY_VIEW_STATE = "view.state"
    }

    private val externalLifecycleRegistry = LifecycleRegistry(this)
    internal val ribLifecycleRegistry = LifecycleRegistry(this)
    internal val viewLifecycleRegistry = LifecycleRegistry(this)
    val detachSignal = BehaviorRelay.create<Unit>()

    val tag: String = this::class.java.name
    val children = CopyOnWriteArrayList<Node<*>>()
    private val lifecycleChildren = CopyOnWriteArrayList<LifecycleChild>()
    private val childrenAttachesRelay: PublishRelay<Node<*>> = PublishRelay.create()
    val childrenAttaches: Observable<Node<*>> = childrenAttachesRelay.hide()

    private val isViewless: Boolean =
        viewFactory == null

    internal open var view: V? = null
    protected var parentViewGroup: ViewGroup? = null

    internal open var savedViewState: SparseArray<Parcelable> = SparseArray()

    internal var isAttachedToView: Boolean = false
        private set

    fun getChildren(): List<Node<*>> =
        children.toList()

    init {
        router.init(this)
    }

    @CallSuper
    open fun onAttach() {
        savedViewState = savedInstanceState?.getSparseParcelableArray<Parcelable>(KEY_VIEW_STATE) ?: SparseArray()

        if (externalLifecycleRegistry.currentState == INITIALIZED) externalLifecycleRegistry.handleLifecycleEvent(ON_CREATE)
        ribLifecycleRegistry.handleLifecycleEvent(ON_CREATE)
        router.onAttach()
        interactor.onAttach(ribLifecycleRegistry)
    }

    open fun attachToView(parentViewGroup: ViewGroup) {
        Log.d("CHECK", "attachToView - $this")
        detachFromView()
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
        if (isAttachedToView) {
            Log.d("CHECK", "detachFromView - $this")
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
    }

    open fun onDetach() {
        Log.d("CHECK", "onDetach - $this")
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

        detachSignal.accept(Unit)
    }

    /**
     * Attaches a child node to this node.
     *
     * @param childNode the [Node] to be attached.
     */
    @MainThread
    internal fun attachChildNode(childDescriptor: Descriptor) {
        Log.d("CHECK", "attachChildNode - $this, $childDescriptor")
        val (childNode, viewAttachMode) = childDescriptor
        children.add(childNode)
        ribRefWatcher.logBreadcrumb(
            "ATTACHED", childNode.javaClass.simpleName, this.javaClass.simpleName
        )

        if (viewAttachMode == AttachMode.PARENT) {
            addParented(childNode)
            childNode.inheritExternalLifecycle(externalLifecycleRegistry)
        }

        childNode.onAttach()
        childrenAttachesRelay.accept(childNode)
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
        lifecycleChildren.remove(childNode)

        ribRefWatcher.watchDeletedObject(childNode)
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
    override fun onStart() {
        externalLifecycleRegistry.handleLifecycleEvent(ON_START)
        onStartInternal { it.onStart() }
    }

    /**
     * To be called only from the hosting environment (Activity, Fragment, etc.)
     *
     * For internal usage call onStopInternal() directly
     */
    override fun onStop() {
        externalLifecycleRegistry.handleLifecycleEvent(ON_STOP)
        onStopInternal { it.onStop() }
    }

    /**
     * To be called only from the hosting environment (Activity, Fragment, etc.)
     *
     * For internal usage call onResumeInternal() directly
     */
    override fun onResume() {
        externalLifecycleRegistry.handleLifecycleEvent(ON_RESUME)
        onResumeInternal { it.onResume() }
    }

    /**
     * To be called only from the hosting environment (Activity, Fragment, etc.)
     *
     * For internal usage call onPauseInternal() directly
     */
    override fun onPause() {
        externalLifecycleRegistry.handleLifecycleEvent(ON_PAUSE)
        onPauseInternal { it.onPause() }
    }

    internal fun onStartInternal(callOnChildren: (Node<*>) -> Unit = { it.onStartInternal() }) {
        Log.d("CHECK", "onStartInternal - $this")
        // The lifecycle cannot go higher than that of the hosting environment (e.g. Activity)
        updateToStateIfViewAttached(externalLifecycleRegistry.currentState)
        lifecycleChildren.forEach { callOnChildren.invoke(it as Node<*>) } // TODO cast is safe, but make it correct
    }

    // TODO call this when overlay is removed
    internal fun onResumeInternal(callOnChildren: (Node<*>) -> Unit = { it.onResumeInternal() }) {
        Log.d("CHECK", "onResumeInternal - $this")
        // The lifecycle cannot go higher than that of the hosting environment (e.g. Activity)
        updateToStateIfViewAttached(externalLifecycleRegistry.currentState)
        lifecycleChildren.forEach { callOnChildren.invoke(it as Node<*>) } // TODO cast is safe, but make it correct
    }

    // TODO call this when overlay hides content AND current rib is not in dialog
    internal fun onPauseInternal(callOnChildren: (Node<*>) -> Unit = { it.onPauseInternal() }) {
        Log.d("CHECK", "onPauseInternal - $this")
        // The lifecycle cannot go higher than that of the hosting environment (e.g. Activity)
        val targetState = if (externalLifecycleRegistry.currentState == CREATED) CREATED else STARTED
        updateToStateIfViewAttached(targetState)
        lifecycleChildren.forEach { callOnChildren.invoke(it as Node<*>) } // TODO cast is safe, but make it correct
    }

    internal fun onStopInternal(callOnChildren: (Node<*>) -> Unit = { it.onStopInternal() }) {
        Log.d("CHECK", "onStopInternal - $this")
        updateToStateIfViewAttached(CREATED)
        lifecycleChildren.forEach { callOnChildren.invoke(it as Node<*>) } // TODO cast is safe, but make it correct
    }

    private fun updateToStateIfViewAttached(state: Lifecycle.State) {
        if (isAttachedToView) {
            ribLifecycleRegistry.markState(state)
            if (!isViewless) {
                view!!.let { viewLifecycleRegistry.markState(state) }
            }
        } else {
            Log.d("CHECK", "SKIPPED updating lifecycle to state: $state - $this, as not attached to view, current state is: ${ribLifecycleRegistry.currentState}")
        }
    }

    /**
     * Dispatch back press to the associated interactor.
     *
     * @return TRUE if the interactor handled the back press and no further action is necessary.
     */
    @CallSuper
    open fun handleBackPress(): Boolean {
        Log.d("CHECK", "handleBackPress - $this")
        ribRefWatcher.logBreadcrumb("BACKPRESS", null, null)
        return children
            .filter { it.shouldPropagateBackPress }
            .any { it.handleBackPress() }
            || interactor.handleBackPress()
            || router.popBackStack()
    }

    /**
     * Valid use-case: RIB is detached from view, but a descendant of it is visible through a [Portal]
     * In this case it should still propagate back press
     */
    private val shouldPropagateBackPress: Boolean
        get() = isAttachedToView || children.any { it.shouldPropagateBackPress }


    internal fun saveViewState() {
        view?.let {
            it.androidView.saveHierarchyState(savedViewState)
        }
    }

    open fun onSaveInstanceState(outState: Bundle) {
        router.onSaveInstanceState(outState)
        interactor.onSaveInstanceState(outState)
        saveViewState()

        val bundle = Bundle()
        bundle.putSparseParcelableArray(KEY_VIEW_STATE, savedViewState)
        outState.putBundle(BUNDLE_KEY, bundle)
    }

    fun onLowMemory() {
        router.onLowMemory()
    }

    override fun getLifecycle(): Lifecycle =
        ribLifecycleRegistry

    override fun toString(): String =
        identifier.toString()
            .substring(
                identifier.toString().lastIndexOf(".")
            )
//                ,
//                identifier.toString().indexOf("$"))
//            .replace(".", "")

    /**
     * Executes an action and remains on the same hierarchical level
     *
     * @return the current workflow element
     */
    protected inline fun <reified T> executeWorkflow(
        crossinline action: () -> Unit
    ): Single<T> = Single.fromCallable {
            action()
            this as T
        }
        .takeUntil(detachSignal.firstOrError())

    @VisibleForTesting
    internal inline fun <reified T> executeWorkflowInternal(
        crossinline action: () -> Unit
    ) : Single<T> = executeWorkflow(action)

    /**
     * Executes an action and transitions to another workflow element
     *
     * @param action an action that's supposed to result in the attach of a child (e.g. router.push())
     *
     * @return the child as the expected workflow element, or error if expected child was not found
     */
    @SuppressWarnings("LongMethod")
    protected inline fun <reified T> attachWorkflow(
        crossinline action: () -> Unit
    ): Single<T> = Single.fromCallable {
            action()
            val childNodesOfExpectedType = children.filterIsInstance<T>()
            if (childNodesOfExpectedType.isEmpty()) {
                Single.error<T>(
                    IllegalStateException(
                        "Expected child of type [${T::class.java}] was not found after executing action. " +
                            "Check that your action actually results in the expected child. " +
                            "Child count: ${children.size}. " +
                            "Last child is: [${children.lastOrNull()}]. " +
                            "All children: $children"
                    )
                )
            } else {
                Single.just(childNodesOfExpectedType.last())
            }
        }
        .flatMap { it }
        .takeUntil(detachSignal.firstOrError())

    @VisibleForTesting
    internal inline fun <reified T> attachWorkflowInternal(
        crossinline action: () -> Unit
    ) : Single<T> = attachWorkflow(action)

    /**
     * Waits until a certain child is attached and returns it as the expected workflow element, or
     * returns it immediately if it's already available.
     *
     * @return the child as the expected workflow element
     */
    protected inline fun <reified T> waitForChildAttached(): Single<T> =
        Single.fromCallable {
            val childNodesOfExpectedType = children.filterIsInstance<T>()
            if (childNodesOfExpectedType.isEmpty()) {
                childrenAttaches.ofType(T::class.java).firstOrError()
            } else {
                Single.just(childNodesOfExpectedType.last())
            }
        }
        .flatMap { it }
        .takeUntil(detachSignal.firstOrError())

    @VisibleForTesting
    internal inline fun <reified T> waitForChildAttachedInternal() : Single<T> =
        waitForChildAttached()
}
