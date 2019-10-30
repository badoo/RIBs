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

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Lifecycle.Event.ON_CREATE
import androidx.lifecycle.Lifecycle.Event.ON_DESTROY
import androidx.lifecycle.Lifecycle.Event.ON_PAUSE
import androidx.lifecycle.Lifecycle.Event.ON_RESUME
import androidx.lifecycle.Lifecycle.Event.ON_START
import androidx.lifecycle.Lifecycle.Event.ON_STOP
import androidx.lifecycle.Lifecycle.State.CREATED
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import android.os.Bundle
import android.os.Parcelable
import androidx.annotation.CallSuper
import androidx.annotation.MainThread
import androidx.annotation.VisibleForTesting
import android.util.SparseArray
import android.view.ViewGroup
import androidx.lifecycle.Lifecycle.State.INITIALIZED
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
import java.lang.IllegalArgumentException
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
    private val viewPlugins: Set<ViewPlugin> = emptySet(),
    private val ribRefWatcher: RibRefWatcher = RibRefWatcher.getInstance()
) : LifecycleOwner {

    enum class AttachMode {
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
        val viewAttachMode: AttachMode
    )

    companion object {
        internal const val BUNDLE_KEY = "Node"
        internal const val KEY_VIEW_STATE = "view.state"
    }

    /**
     * FIXME the proper solution is to set this in constructor (pack it with savedInstanceState)
     * If left like this, it's not guaranteed to be set correctly, and can lead to problems
     * Proposed solution would mean passing Root only at integration point, Child is used automatically
     * by building mechanism.
     * Also PortalRouter.Configuration.Portal can then work directly with a @Parcelize AncestryInfo,
     * which is not currently possible.
     */
    var ancestryInfo: AncestryInfo = AncestryInfo.Root
    val resolver: ConfigurationResolver<*, V> = router
    private val savedInstanceState = savedInstanceState?.getBundle(BUNDLE_KEY)
    internal open val externalLifecycleRegistry = LifecycleRegistry(this)
    internal open val virtualRibLifecycle = LifecycleRegistry(this)
    internal open var virtualViewLifecycle: LifecycleRegistry? = null
    internal val effectiveRibLifecycle = LifecycleRegistry(this)
    internal var effectiveViewLifecycle: LifecycleRegistry? = null
    val detachSignal = BehaviorRelay.create<Unit>()

    val tag: String = this::class.java.name
    val children = CopyOnWriteArrayList<Node<*>>()
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

    /**
     * To be used internally to update either external, or one of the virtual lifecycles.
     *
     * After change, effective lifecycles are updated to capped values, which is sent to client code.
     *
     * In effect this means that whenever virtual lifecycles or external lifecycles change,
     * the effective lifecycles are updated to min(external, virtual) and enforced to be at least
     * in CREATED state.
     */
    private fun handleLifecycleEvent(registry: LifecycleRegistry, event: Lifecycle.Event) {
        registry.handleLifecycleEvent(event)
        effectiveRibLifecycle.markState(capLifecycle(externalLifecycleRegistry, virtualRibLifecycle))
        effectiveViewLifecycle?.markState(capLifecycle(externalLifecycleRegistry, virtualViewLifecycle))
    }

    /**
     * Takes the minimum of the two lifecycles, and enforces it to at least CREATED level.
     * (We do not track INITIALIZED state)
     */
    private fun capLifecycle(reg1: LifecycleRegistry, reg2: LifecycleRegistry?): Lifecycle.State =
        atLeastCreated(
            min(reg1.currentState, reg2?.currentState)
        )

    private fun atLeastCreated(state: Lifecycle.State): Lifecycle.State =
        if (state == INITIALIZED) CREATED else state

    private fun min(state1: Lifecycle.State, state2: Lifecycle.State?): Lifecycle.State =
        if (state2 != null && state2 < state1) state2 else state1

    @CallSuper
    open fun onAttach() {
        savedViewState = savedInstanceState?.getSparseParcelableArray<Parcelable>(KEY_VIEW_STATE) ?: SparseArray()

        // RIBs without view will always be at: min(RESUMED, external)
        if (isViewless) onResume(virtualRibLifecycle) else onCreate(virtualRibLifecycle)
        router.onAttach()
        interactor.onAttach(effectiveRibLifecycle)
    }

    fun attachToView(parentViewGroup: ViewGroup) {
        detachFromView()
        this.parentViewGroup = parentViewGroup
        isAttachedToView = true

        if (!isViewless) {
            createView(parentViewGroup)
        }

        setupLifecycleOnViewCreate()
        view?.let {
            interactor.onViewCreated(effectiveViewLifecycle!!, it)
        }
        router.onAttachView()
        viewPlugins.forEach { it.onAttachtoView(parentViewGroup) }
    }

    private fun setupLifecycleOnViewCreate() {
        // These should be set even for viewless Nodes so that
        // lifecycle propagation to children will work.
        virtualViewLifecycle = LifecycleRegistry(this)
        effectiveViewLifecycle = LifecycleRegistry(this)
        onCreate(virtualViewLifecycle!!)
        if (!isViewless) {
            // If isViewless, it's already RESUMED in onAttach()
            onStart(virtualRibLifecycle)
            onResume(virtualRibLifecycle)
        }
        onStart(virtualViewLifecycle!!)
        onResume(virtualViewLifecycle!!)
    }

    private fun createView(parentViewGroup: ViewGroup) {
        view = viewFactory?.invoke(parentViewGroup)
        view!!.let { view ->
            parentViewGroup.addView(view.androidView)
            view.androidView.restoreHierarchyState(savedViewState)
        }
    }

    fun detachFromView() {
        if (isAttachedToView) {
            router.onDetachView()
            tearDownLifecycleOnViewDestroy()

            if (!isViewless) {
                parentViewGroup!!.removeView(view!!.androidView)
            }

            viewPlugins.forEach {
                it.onDetachFromView(parentViewGroup!!)
            }

            view = null
            isAttachedToView = false
            this.parentViewGroup = null
        }
    }

    private fun tearDownLifecycleOnViewDestroy() {
        if (isViewless) {
            // Implication: if RIB is viewless, we're keeping it resumed capped by external lifecycle only
            return
        }

        virtualViewLifecycle!!.let {
            onPause(it)
            onStop(it)
            onDestroy(it)
        }

        virtualViewLifecycle = null
        effectiveViewLifecycle = null

        virtualRibLifecycle.let {
            onPause(it)
            onStop(it)
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

        onDestroy(virtualRibLifecycle)
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
     * For internal usage call onStart() with proper inner lifecycle registry directly
     */
    fun onStart() {
        onStart(externalLifecycleRegistry)
    }

    /**
     * To be called only from the hosting environment (Activity, Fragment, etc.)
     *
     * For internal usage call onStop() with proper inner lifecycle registry directly
     */
    fun onStop() {
        onStop(externalLifecycleRegistry)
    }

    /**
     * To be called only from the hosting environment (Activity, Fragment, etc.)
     *
     * For internal usage call onResume() with proper inner lifecycle registry directly
     */
    fun onResume() {
        onResume(externalLifecycleRegistry)
    }

    /**
     * To be called only from the hosting environment (Activity, Fragment, etc.)
     *
     * For internal usage call onPause() with proper inner lifecycle registry directly
     */
    fun onPause() {
        onPause(externalLifecycleRegistry)
    }

    /**
     * Called only from onAttach() / attachToView(), not tied to external onCreate()
     */
    private fun onCreate(registry: LifecycleRegistry) {
        handleLifecycleEvent(registry, ON_CREATE)
        // We do NOT pass this to children. They will be created later.
    }

    internal fun onStart(registry: LifecycleRegistry) {
        handleLifecycleEvent(registry, ON_START)
        updateChildren(registry) { child, targetRegistry ->
            child.onStart(targetRegistry)
        }
    }

    // TODO call this when overlay is removed
    internal fun onResume(registry: LifecycleRegistry) {
        handleLifecycleEvent(registry, ON_RESUME)
        updateChildren(registry) { child, targetRegistry ->
            child.onResume(targetRegistry)
        }
    }

    // TODO call this when overlay hides content AND current rib is not in dialog
    internal fun onPause(registry: LifecycleRegistry) {
        handleLifecycleEvent(registry, ON_PAUSE)
        updateChildren(registry) { child, targetRegistry ->
            child.onPause(targetRegistry)
        }
    }

    internal fun onStop(registry: LifecycleRegistry) {
        handleLifecycleEvent(registry, ON_STOP)
        updateChildren(registry) { child, targetRegistry ->
            child.onStop(targetRegistry)
        }
    }

    /**
     * Called only from onDetach() / detachFromView(), not tied to external onDestroy()
     */
    private fun onDestroy(registry: LifecycleRegistry) {
        handleLifecycleEvent(registry, ON_DESTROY)
        updateChildren(registry) { child, targetRegistry ->
            child.onDestroy(targetRegistry)
        }
    }

    /**
     * Matches incoming LifecycleRegistry to its equivalent in all children, and calls passed in
     * method on children with that target registry as parameter.
     *
     * To be used for recursively calling lifecycle updates on children.
     */
    private fun updateChildren(
        parentRegistry: LifecycleRegistry,
        block: (child: Node<*>, childRegistry: LifecycleRegistry) -> Unit
    ) {
        children.forEach { child ->
            targetChildRegistry(parentRegistry, child)?.let { targetRegistry ->
                block.invoke(child, targetRegistry)
            }
        }
    }

    /**
     * Matches incoming LifecycleRegistry to its equivalent in a child.
     */
    private fun targetChildRegistry(
        registry: LifecycleRegistry,
        child: Node<*>
    ): LifecycleRegistry? =
        when (registry) {
            externalLifecycleRegistry -> child.externalLifecycleRegistry
            virtualRibLifecycle -> child.virtualRibLifecycle
            virtualViewLifecycle -> child.virtualViewLifecycle
            else -> throw IllegalArgumentException(
                "This should never happen. Only external and virtual registries should be targeted"
            )
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
        effectiveRibLifecycle

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
