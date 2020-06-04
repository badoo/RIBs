package com.badoo.ribs.core

import android.os.Bundle
import android.os.Parcelable
import android.util.SparseArray
import android.view.ViewGroup
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
import com.badoo.ribs.core.plugin.AndroidLifecycleAware
import com.badoo.ribs.core.plugin.BackPressHandler
import com.badoo.ribs.core.plugin.NodeAware
import com.badoo.ribs.core.plugin.NodeLifecycleAware
import com.badoo.ribs.core.plugin.Plugin
import com.badoo.ribs.core.plugin.SavesInstanceState
import com.badoo.ribs.core.plugin.SubtreeBackPressHandler
import com.badoo.ribs.core.plugin.SubtreeChangeAware
import com.badoo.ribs.core.plugin.SubtreeViewChangeAware
import com.badoo.ribs.core.plugin.SystemAware
import com.badoo.ribs.core.plugin.ViewAware
import com.badoo.ribs.core.plugin.ViewLifecycleAware
import com.badoo.ribs.core.view.RibView
import com.badoo.ribs.util.RIBs
import com.jakewharton.rxrelay2.BehaviorRelay
import com.jakewharton.rxrelay2.PublishRelay
import io.reactivex.Observable
import io.reactivex.Single
import java.util.concurrent.CopyOnWriteArrayList

/**
 * Responsible for handling the addition and removal of child nodes.
 **/
@SuppressWarnings("LargeClass")
open class Node<V : RibView>(
    val buildParams: BuildParams<*>,
    private val viewFactory: ((ViewGroup) -> V?)?,
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

    /**
     * TODO PortalRouter.Configuration.Portal can then work directly with a @Parcelize AncestryInfo,
     *  which was not possible until now.
     */
    internal val ancestryInfo: AncestryInfo =
        buildContext.ancestryInfo

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
    internal var parentViewGroup: ViewGroup? = null

    internal open var savedViewState: SparseArray<Parcelable> =
        savedInstanceState?.getSparseParcelableArray<Parcelable>(KEY_VIEW_STATE) ?: SparseArray()

    internal var isAttachedToView: Boolean = false
        private set

    private var isPendingViewDetach: Boolean = false
    private var isPendingDetach: Boolean = false

    fun getChildren(): List<Node<*>> =
        children.toList()

    init {
        plugins.filterIsInstance<NodeAware>().forEach { it.init(this) }
    }

    internal fun onCreate() {
        parent?.onChildCreated(this)
        plugins.filterIsInstance<NodeLifecycleAware>().forEach { it.onCreate() }
    }

    @CallSuper
    open fun onAttach() {
        lifecycleManager.onCreateRib()
        plugins.filterIsInstance<NodeLifecycleAware>().forEach { it.onAttach(lifecycleManager.ribLifecycle.lifecycle) }
    }

    fun attachToView(parentViewGroup: ViewGroup) {
        detachFromView()
        this.parentViewGroup = parentViewGroup
        isAttachedToView = true

        if (!isViewless) {
            createView(parentViewGroup)?.let {
                parentViewGroup.attach(it)
            }
        }

        lifecycleManager.onCreateView()
        view?.let { view ->
            plugins.filterIsInstance<ViewAware<V>>().forEach {
                it.onViewCreated(view, lifecycleManager.viewLifecycle!!.lifecycle)
            }
        }
        plugins.filterIsInstance<ViewLifecycleAware>().forEach { it.onAttachToView(parentViewGroup) }
    }

    private fun createView(parentViewGroup: ViewGroup): V? {
        if (view == null) {
            view = viewFactory?.invoke(parentViewGroup)
        }

        return view
    }

    private fun ViewGroup.attach(view: V) {
        addView(view.androidView)
        view.androidView.restoreHierarchyState(savedViewState)
    }

    internal fun createChildView(child: Node<*>) {
        if (isAttachedToView) {
            child.createView(
                // parentViewGroup is guaranteed to be non-null if and only if view is attached
                (view?.getParentViewForChild(child) ?: parentViewGroup!!)
            )
        }
    }

    fun detachFromView() {
        if (isAttachedToView) {
            plugins.filterIsInstance<ViewLifecycleAware>().forEach { it.onDetachFromView(parentViewGroup!!) }
            lifecycleManager.onDestroyView()

            if (!isViewless) {
                parentViewGroup!!.removeView(view!!.androidView)
            }

            view = null
            isAttachedToView = false
            this.parentViewGroup = null
            isPendingViewDetach = false
        }
    }

    open fun onDetach() {
        if (isAttachedToView) {
            RIBs.errorHandler.handleNonFatalError(
                "View was not detached before node detach!",
                RuntimeException("View was not detached before node detach! RIB: $this")
            )
            detachFromView()
        }

        lifecycleManager.onDestroyRib()
        plugins.filterIsInstance<NodeLifecycleAware>().forEach { it.onDetach() }

        for (child in children) {
            detachChildNode(child)
        }

        detachSignal.accept(Unit)
        isPendingDetach = false
    }

    fun onChildCreated(child: Node<*>) {
        plugins.filterIsInstance<SubtreeChangeAware>().forEach { it.onChildCreated(child) }
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
        if (child.ancestryInfo is AncestryInfo.Root) {
            val message = "A node that is attached as a child should not have a root BuildContext."
            RIBs.errorHandler.handleNonFatalError(
                errorMessage = message,
                throwable = RootNodeAttachedAsChildException(message = "$message. RIB: $this")
            )
        }
    }

    // FIXME internal + protected?
    fun attachChildView(child: Node<*>) {
        if (isAttachedToView) {
            val target = targetViewGroupForChild(child)
            child.attachToView(target)
            plugins.filterIsInstance<SubtreeViewChangeAware>().forEach { it.onAttachChildView(child) }
        }
    }

    internal fun targetViewGroupForChild(child: Node<*>): ViewGroup {
        return when {
            // parentViewGroup is guaranteed to be non-null if and only if view is attached
            isViewless -> parentViewGroup!!
            else -> view!!.getParentViewForChild(child) ?: parentViewGroup!!
        }
    }

    // FIXME internal + protected?
    fun detachChildView(child: Node<*>) {
        child.detachFromView()
        plugins.filterIsInstance<SubtreeViewChangeAware>().forEach { it.onDetachChildView(child) }
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
            .filter { it.isAttachedToView && !(it.isPendingDetach || it.isPendingViewDetach ) }
            .any { it.handleBackPress() }

    internal fun saveViewState() {
        view?.let {
            it.androidView.saveHierarchyState(savedViewState)
        }
    }

    open fun onSaveInstanceState(outState: Bundle) {
        outState.putSerializable(Identifier.KEY_UUID, identifier.uuid)
        plugins.filterIsInstance<SavesInstanceState>().forEach { it.onSaveInstanceState(outState) }
        saveViewState()

        val bundle = Bundle()
        bundle.putSparseParcelableArray(KEY_VIEW_STATE, savedViewState)
        outState.putBundle(BUNDLE_KEY, bundle)
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
