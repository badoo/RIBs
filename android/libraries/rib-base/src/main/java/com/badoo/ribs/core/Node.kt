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

import android.os.Bundle
import android.os.Parcelable
import android.util.SparseArray
import android.view.ViewGroup
import androidx.annotation.CallSuper
import androidx.annotation.MainThread
import androidx.annotation.VisibleForTesting
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Lifecycle.Event.ON_CREATE
import androidx.lifecycle.Lifecycle.Event.ON_DESTROY
import androidx.lifecycle.Lifecycle.Event.ON_PAUSE
import androidx.lifecycle.Lifecycle.Event.ON_RESUME
import androidx.lifecycle.Lifecycle.Event.ON_START
import androidx.lifecycle.Lifecycle.Event.ON_STOP
import androidx.lifecycle.Lifecycle.State.CREATED
import androidx.lifecycle.Lifecycle.State.INITIALIZED
import androidx.lifecycle.Lifecycle.State.STARTED
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import com.badoo.ribs.core.Rib.Identifier
import com.badoo.ribs.core.routing.configuration.ConfigurationResolver
import com.badoo.ribs.core.routing.portal.AncestryInfo
import com.badoo.ribs.core.view.RibView
import com.badoo.ribs.core.view.ViewPlugin
import com.badoo.ribs.util.RIBs
import com.jakewharton.rxrelay2.BehaviorRelay
import com.jakewharton.rxrelay2.PublishRelay
import com.uber.rib.util.RibRefWatcher
import io.reactivex.Observable
import io.reactivex.Single
import java.util.concurrent.CopyOnWriteArrayList

/**
 * Responsible for handling the addition and removal of child nodes.
 **/
@SuppressWarnings("LargeClass")
open class Node<V : RibView>(
    buildContext: BuildContext.Resolved<*>,
    private val viewFactory: ((ViewGroup) -> V?)?,
    private val router: Router<*, *, *, *, V>,
    private val interactor: Interactor<*, *, *, V>,
    private val viewPlugins: Set<ViewPlugin> = emptySet(),
    private val ribRefWatcher: RibRefWatcher = RibRefWatcher.getInstance()
) : LifecycleOwner {

    companion object {
        internal const val BUNDLE_KEY = "Node"
        internal const val KEY_VIEW_STATE = "view.state"
    }

    internal open val identifier: Rib.Identifier =
        buildContext.identifier

    internal val ancestryInfo: AncestryInfo =
        buildContext.ancestryInfo

    internal open val viewAttachMode: AttachMode =
        buildContext.viewAttachMode

    val resolver: ConfigurationResolver<*, V> = router
    private val savedInstanceState = buildContext.savedInstanceState?.getBundle(BUNDLE_KEY)
    internal val externalLifecycleRegistry = LifecycleRegistry(this)
    internal val ribLifecycleRegistry = LifecycleRegistry(this)
    internal var viewLifecycleRegistry: LifecycleRegistry? = null
    val detachSignal = BehaviorRelay.create<Unit>()

    val tag: String = this::class.java.name
    val children = CopyOnWriteArrayList<Node<*>>()
    private val childrenAttachesRelay: PublishRelay<Node<*>> = PublishRelay.create()
    val childrenAttaches: Observable<Node<*>> = childrenAttachesRelay.hide()

    private val isViewless: Boolean =
        viewFactory == null

    internal open var view: V? = null
    protected var parentViewGroup: ViewGroup? = null

    internal open var savedViewState: SparseArray<Parcelable> =
        savedInstanceState?.getSparseParcelableArray<Parcelable>(KEY_VIEW_STATE) ?: SparseArray()

    internal var isAttachedToView: Boolean = false
        private set

    fun getChildren(): List<Node<*>> =
        children.toList()

    init {
        router.init(this)
    }

    @CallSuper
    open fun onAttach() {
        if (externalLifecycleRegistry.currentState == INITIALIZED) externalLifecycleRegistry.handleLifecycleEvent(ON_CREATE)
        ribLifecycleRegistry.handleLifecycleEvent(ON_CREATE)
        router.onAttach()
        interactor.onAttach(ribLifecycleRegistry)
    }

    fun attachToView(parentViewGroup: ViewGroup) {
        detachFromView()
        this.parentViewGroup = parentViewGroup
        isAttachedToView = true

        if (!isViewless) {
            createView(parentViewGroup)
        }

        router.onAttachView()
        viewPlugins.forEach { it.onAttachtoView(parentViewGroup) }
        onStartInternal()
        onResumeInternal()
    }

    private fun createView(parentViewGroup: ViewGroup) {
        view = viewFactory?.invoke(parentViewGroup)
        view!!.let { view ->
            parentViewGroup.addView(view.androidView)
            viewLifecycleRegistry = LifecycleRegistry(this).apply {
                // At this point externalLifecycleRegistry is at least CREATED, otherwise we wouldn't be attaching to view
                markState(externalLifecycleRegistry.currentState)
                interactor.onViewCreated(this, view)
            }
            view.androidView.restoreHierarchyState(savedViewState)
        }
    }

    fun detachFromView() {
        if (isAttachedToView) {
            onPauseInternal()
            onStopInternal()
            router.onDetachView()

            if (!isViewless) {
                view!!.let {
                    parentViewGroup!!.removeView(it.androidView)
                    viewLifecycleRegistry?.handleLifecycleEvent(ON_DESTROY)
                    viewLifecycleRegistry = null
                }
            }

            viewPlugins.forEach { it.onDetachFromView(parentViewGroup!!) }
            view = null
            isAttachedToView = false
            this.parentViewGroup = null
        }
    }

    open fun onDetach() {
        if (isAttachedToView) {
            RIBs.errorHandler.handleNonFatalError(
                "View was not detached before node detach!",
                RuntimeException("View was not detached before node detach!")
            )
            detachFromView()
        }

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
    internal fun attachChildNode(child: Node<*>) {
        children.add(child)
        ribRefWatcher.logBreadcrumb(
            "ATTACHED", child.javaClass.simpleName, this.javaClass.simpleName
        )

        child.inheritExternalLifecycle(externalLifecycleRegistry)
        child.onAttach()
        childrenAttachesRelay.accept(child)
    }

    private fun inheritExternalLifecycle(lifecycleRegistry: LifecycleRegistry) {
        externalLifecycleRegistry.markState(lifecycleRegistry.currentState)
        children.forEach {
            it.inheritExternalLifecycle(lifecycleRegistry)
        }
    }

    // FIXME internal + protected?
    fun attachChildView(child: Node<*>) {
        if (isAttachedToView) {
            val target = when {
                // parentViewGroup is guaranteed to be non-null if and only if view is attached
                isViewless -> parentViewGroup!!
                else -> view!!.getParentViewForChild(child.identifier) ?: parentViewGroup!!
            }

            child.attachToView(target)
        }
    }

    // FIXME internal + protected?
    fun detachChildView(child: Node<*>) {
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
                viewLifecycleRegistry!!.markState(state)
            }
        }
    }

    @CallSuper
    open fun handleBackPress(): Boolean {
        ribRefWatcher.logBreadcrumb("BACKPRESS", null, null)
        return router.popOverlay()
            || delegateHandleBackPressToActiveChildren()
            || interactor.handleBackPress()
            || router.popBackStack()
    }

    private fun delegateHandleBackPressToActiveChildren(): Boolean =
        children
            .filter { it.isAttachedToView }
            .any { it.handleBackPress() }

    internal fun saveViewState() {
        view?.let {
            it.androidView.saveHierarchyState(savedViewState)
        }
    }

    open fun onSaveInstanceState(outState: Bundle) {
        outState.putSerializable(Identifier.KEY_UUID, identifier.uuid)
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
