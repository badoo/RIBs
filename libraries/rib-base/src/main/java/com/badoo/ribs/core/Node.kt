package com.badoo.ribs.core

import android.os.Bundle
import android.os.Parcelable
import android.util.SparseArray
import androidx.annotation.CallSuper
import androidx.annotation.MainThread
import androidx.annotation.VisibleForTesting
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import com.badoo.ribs.core.Rib.Identifier
import com.badoo.ribs.core.exception.RootNodeAttachedAsChildException
import com.badoo.ribs.core.lifecycle.LifecycleManager
import com.badoo.ribs.core.modality.ActivationMode
import com.badoo.ribs.core.modality.AncestryInfo
import com.badoo.ribs.core.modality.BuildContext
import com.badoo.ribs.core.modality.BuildParams
import com.badoo.ribs.core.plugin.*
import com.badoo.ribs.core.view.RibView
import com.badoo.ribs.util.RIBs
import com.jakewharton.rxrelay2.BehaviorRelay
import com.jakewharton.rxrelay2.PublishRelay
import io.reactivex.Observable
import io.reactivex.Single
import java.util.concurrent.CopyOnWriteArrayList

/**
 * The main structure element of the system.
 *
 * Not responsible for making decisions itself.
 *
 * Offers operations to manage children.
 * Maintains lifecycle state.
 * Forwards events to plugins / children respectively.
 **/
@SuppressWarnings("LargeClass")
open class Node<V : RibView>(
    val buildParams: BuildParams<*>,
    private val viewFactory: ((RibView) -> V?)?, // TODO V? vs V
    private val plugins: List<Plugin> = emptyList()
) : Rib, LifecycleOwner {

    companion object {
        internal const val BUNDLE_KEY = "Node"
        internal const val KEY_VIEW_STATE = "view.state"
    }

    final override val node: Node<V>
        get() = this

    open val identifier: Rib.Identifier =
        buildParams.identifier

    internal val buildContext: BuildContext =
        buildParams.buildContext

    val ancestryInfo: AncestryInfo =
        buildContext.ancestryInfo

    val isRoot: Boolean =
        ancestryInfo == AncestryInfo.Root

    /**
     * This is the logical parent of the current [Node] (i.e. the one that created it).
     *
     * Not necessarily the parentNode the view is attached to (e.g. portals).
     */
    val parent: Node<*>? =
        when (val ancestryInfo = ancestryInfo) {
            is AncestryInfo.Root -> null
            is AncestryInfo.Child -> ancestryInfo.anchor
        }

    internal open val activationMode: ActivationMode =
        buildContext.activationMode

    private val savedInstanceState = buildParams.savedInstanceState?.getBundle(BUNDLE_KEY)
    internal val externalLifecycleRegistry = LifecycleRegistry(this)
    val detachSignal = BehaviorRelay.create<Unit>()

    val tag: String = this::class.java.name
    val children = CopyOnWriteArrayList<Node<*>>()
    private val childrenAttachesRelay: PublishRelay<Node<*>> = PublishRelay.create()
    val childrenAttaches: Observable<Node<*>> = childrenAttachesRelay.hide()

    internal open val lifecycleManager = LifecycleManager(this)

    internal val isViewless: Boolean =
        viewFactory == null

    internal open var view: V? = null
    private var rootHost: RibView? = null

    internal open var savedViewState: SparseArray<Parcelable> =
        savedInstanceState?.getSparseParcelableArray<Parcelable>(KEY_VIEW_STATE) ?: SparseArray()

    internal var isAttachedToView: Boolean = false
        private set

    private var isPendingViewDetach: Boolean = false
    private var isPendingDetach: Boolean = false

    private val isActive: Boolean
        get() = isAttachedToView && !isPendingViewDetach && !isPendingDetach


    fun getChildren(): List<Node<*>> =
        children.toList()

    init {
        plugins.filterIsInstance<NodeAware>().forEach { it.init(this) }
    }

    internal fun onCreate() {
        parent?.onChildCreated(this)
        plugins.filterIsInstance<NodeLifecycleAware>().forEach { it.onCreate() }
    }

    private fun onChildCreated(child: Node<*>) {
        plugins.filterIsInstance<SubtreeChangeAware>().forEach { it.onChildCreated(child) }
    }

    @CallSuper
    open fun onAttach() {
        lifecycleManager.onCreateRib()
        plugins.filterIsInstance<NodeLifecycleAware>().forEach { it.onAttach(lifecycleManager.ribLifecycle.lifecycle) }
    }

    fun onCreateView(parentView: RibView): V? {
        if (isRoot) rootHost = parentView
        if (view == null) {
            view = viewFactory?.invoke(parentView)
            view?.let { view ->
                view.androidView.restoreHierarchyState(savedViewState)
                lifecycleManager.onViewCreated()
                plugins.filterIsInstance<ViewAware<V>>().forEach {
                    it.onViewCreated(view, lifecycleManager.viewLifecycle!!.lifecycle)
                }
            }
        }

        return view
    }

    fun onAttachToView() {
        onAttachToViewChecks()
        isAttachedToView = true
        lifecycleManager.onAttachToView()
        plugins.filterIsInstance<ViewLifecycleAware>().forEach { it.onAttachToView() }
    }

    private fun onAttachToViewChecks() {
        if (!isViewless && view == null) {
            error("Trying to run onAttachToView() expecting a view, but view wasn't created")
        }

        if (isAttachedToView) {
            RIBs.errorHandler.handleNonFatalError(
                "View is already attached to some view, it should be detached first. RIB: $this",
                RuntimeException("View is already attached to some view, it should be detached first. RIB: $this")
            )
        }
    }

    fun onDetachFromView() {
        if (isAttachedToView) {
            plugins.filterIsInstance<ViewLifecycleAware>().forEach { it.onDetachFromView() }
            lifecycleManager.onDetachFromView()
            saveViewState()
            isAttachedToView = false
            isPendingViewDetach = false
            rootHost = null
            view = null
        }
    }

    open fun onDetach() {
        if (view != null) {
            RIBs.errorHandler.handleNonFatalError(
                "View was not detached before node detach!",
                RuntimeException("View was not detached before node detach! RIB: $this")
            )
        }

        lifecycleManager.onDestroyRib()
        plugins.filterIsInstance<NodeLifecycleAware>().forEach { it.onDetach() }

        for (child in children) {
            detachChildNode(child)
        }

        detachSignal.accept(Unit)
        isPendingDetach = false
    }

    /**
     * Attaches a child node to this node.
     *
     * @param child the [Node] to be attached.
     */
    @MainThread
    internal fun attachChildNode(child: Node<*>) {
        verifyNotRoot(child)
        children.add(child)
        lifecycleManager.onAttachChild(child)
        child.onAttach()
        childrenAttachesRelay.accept(child)
        plugins.filterIsInstance<SubtreeChangeAware>().forEach { it.onAttachChild(child) }
    }

    private fun verifyNotRoot(child: Node<*>) {
        if (child.isRoot) {
            val message = "A node that is attached as a child should not have a root BuildContext."
            RIBs.errorHandler.handleNonFatalError(
                errorMessage = message,
                throwable = RootNodeAttachedAsChildException(message = "$message. RIB: $this")
            )
        }
    }

    fun attachChildView(child: Node<*>) {
        attachChildView(child, true)
    }

    private fun attachChildView(child: Node<*>, notifyPlugins: Boolean) {
        if (isAttachedToView) {
            view?.let { it.attachChild(child) }
                ?: parent?.attachChildView(child, false)
                ?: rootHost?.attachChild(child)
                ?: error("No view, no parent, and no root host should be technically impossible")

            if (notifyPlugins) plugins.filterIsInstance<SubtreeViewChangeAware>()
                .forEach { it.onAttachChildView(child) }
        }
    }

    fun detachChildView(child: Node<*>) {
        detachChildView(child, true)
    }

    private fun detachChildView(child: Node<*>, notifyPlugins: Boolean) {
        if (isAttachedToView) {
            view?.let { it.detachChild(child) }
                ?: parent?.detachChildView(child, false)
                ?: rootHost!!.detachChild(child)
                ?: error("No view, no parent, and no root host should be technically impossible")

            if (notifyPlugins) plugins.filterIsInstance<SubtreeViewChangeAware>()
                .forEach { it.onDetachChildView(child) }
        }
    }



    /**
     * Detaches the node from this parent. NOTE: No consumers of
     * this API should ever keep a reference to the detached child, leak canary will enforce
     * that it gets garbage collected.
     *
     * @param child the [Node] to be detached.
     */
    @MainThread
    internal fun detachChildNode(child: Node<*>) {
        plugins.filterIsInstance<SubtreeChangeAware>().forEach { it.onDetachChild(child) }
        children.remove(child)
        child.onDetach()
    }

    internal fun markPendingViewDetach(isPendingViewDetach: Boolean) {
        this.isPendingViewDetach = isPendingViewDetach
    }

    internal fun markPendingDetach(isPendingDetach: Boolean) {
        this.isPendingDetach = isPendingDetach
    }

    /**
     * To be called from the hosting environment (Activity, Fragment, etc.)
     */
    fun onStart() {
        lifecycleManager.onStartExternal()
        plugins.filterIsInstance<AndroidLifecycleAware>().forEach { it.onStart() }
    }

    /**
     * To be called from the hosting environment (Activity, Fragment, etc.)
     */
    fun onStop() {
        lifecycleManager.onStopExternal()
        plugins.filterIsInstance<AndroidLifecycleAware>().forEach { it.onStop() }
    }

    /**
     * To be called from the hosting environment (Activity, Fragment, etc.)
     */
    fun onResume() {
        lifecycleManager.onResumeExternal()
        plugins.filterIsInstance<AndroidLifecycleAware>().forEach { it.onResume() }
    }

    /**
     * To be called from the hosting environment (Activity, Fragment, etc.)
     */
    fun onPause() {
        lifecycleManager.onPauseExternal()
        plugins.filterIsInstance<AndroidLifecycleAware>().forEach { it.onPause() }
    }

    @CallSuper
    open fun handleBackPress(): Boolean {
        val subtreeHandlers = plugins.filterIsInstance<SubtreeBackPressHandler>()
        val handlers = plugins.filterIsInstance<BackPressHandler>()

        return subtreeHandlers.any { it.handleBackPressFirst() }
            || delegateHandleBackPressToActiveChildren()
            || handlers.any { it.handleBackPress() }
            || subtreeHandlers.any { it.handleBackPressFallback() }
    }

    private fun delegateHandleBackPressToActiveChildren(): Boolean =
        children
            .filter { it.isActive }
            .any { it.handleBackPress() }

    open fun onSaveInstanceState(outState: Bundle) {
        outState.putSerializable(Identifier.KEY_UUID, identifier.uuid)
        plugins.filterIsInstance<SavesInstanceState>().forEach { it.onSaveInstanceState(outState) }
        saveViewState()

        val bundle = Bundle()
        bundle.putSparseParcelableArray(KEY_VIEW_STATE, savedViewState)
        outState.putBundle(BUNDLE_KEY, bundle)
    }

    fun saveViewState() {
        view?.let {
            it.androidView.saveHierarchyState(savedViewState)
        }
    }

    fun onLowMemory() {
        plugins.filterIsInstance<SystemAware>().forEach { it.onLowMemory() }
    }

    override fun getLifecycle(): Lifecycle =
        lifecycleManager.lifecycle

    fun <P> plugins(pClass: Class<P>): List<P> =
        plugins.filterIsInstance(pClass)

    inline fun <reified P> plugins(): List<P> =
        plugins(P::class.java)

    fun <P> plugin(pClass: Class<P>): P? =
        plugins(pClass).firstOrNull()

    inline fun <reified P> plugin(): P? =
        plugin(P::class.java)

    inline fun <reified P> pluginUp(): P? {
        var found: P?
        var current: Node<*>? = parent

        while (current != null) {
            found = current.plugin<P>()
            if (found != null) return found
            current = current.parent
        }

        return null
    }

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
