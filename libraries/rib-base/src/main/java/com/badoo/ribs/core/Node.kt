package com.badoo.ribs.core

import android.os.Bundle
import android.os.Looper
import androidx.annotation.CallSuper
import androidx.annotation.MainThread
import androidx.annotation.VisibleForTesting
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import com.badoo.ribs.android.integrationpoint.FloatingIntegrationPoint
import com.badoo.ribs.android.integrationpoint.IntegrationPoint
import com.badoo.ribs.core.Rib.Identifier
import com.badoo.ribs.core.exception.RootNodeAttachedAsChildException
import com.badoo.ribs.core.lifecycle.LifecycleManager
import com.badoo.ribs.core.modality.ActivationMode
import com.badoo.ribs.core.modality.AncestryInfo
import com.badoo.ribs.core.modality.BuildContext
import com.badoo.ribs.core.modality.BuildParams
import com.badoo.ribs.core.plugin.AndroidLifecycleAware
import com.badoo.ribs.core.plugin.BackPressHandler
import com.badoo.ribs.core.plugin.GlobalNodeLifecycleAware
import com.badoo.ribs.core.plugin.NodeAware
import com.badoo.ribs.core.plugin.NodeLifecycleAware
import com.badoo.ribs.core.plugin.Plugin
import com.badoo.ribs.core.plugin.SavesInstanceState
import com.badoo.ribs.core.plugin.SubtreeBackPressHandler
import com.badoo.ribs.core.plugin.SubtreeChangeAware
import com.badoo.ribs.core.plugin.SubtreeViewChangeAware
import com.badoo.ribs.core.plugin.SystemAware
import com.badoo.ribs.core.plugin.UpNavigationHandler
import com.badoo.ribs.core.plugin.ViewAware
import com.badoo.ribs.core.plugin.ViewLifecycleAware
import com.badoo.ribs.core.view.RibView
import com.badoo.ribs.core.view.ViewFactory
import com.badoo.ribs.store.RetainedInstanceStore
import com.badoo.ribs.util.RIBs

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
open class Node<V : RibView> @VisibleForTesting internal constructor(
    val buildParams: BuildParams<*>,
    private val viewFactory: ViewFactory<V>?,
    private val retainedInstanceStore: RetainedInstanceStore,
    private val isOnMainThreadFunc: () -> Boolean,
    plugins: List<Plugin> = emptyList()
) : Rib, LifecycleOwner {

    companion object {
        internal const val KEY_VIEW_STATE = "view.state"
    }

    constructor(
        buildParams: BuildParams<*>,
        viewFactory: ViewFactory<V>?,
        plugins: List<Plugin> = emptyList()
    ) : this(
        buildParams = buildParams,
        viewFactory = viewFactory,
        retainedInstanceStore = RetainedInstanceStore,
        isOnMainThreadFunc = { Looper.getMainLooper() == Looper.myLooper() },
        plugins = plugins
    )

    final override val node: Node<V>
        get() = this

    open val identifier: Identifier =
        buildParams.identifier

    internal val buildContext: BuildContext =
        buildParams.buildContext

    var integrationPoint: IntegrationPoint = FloatingIntegrationPoint()
        internal set
        get() {
            return if (isRoot) field
            else parent?.integrationPoint ?: RIBs.errorHandler.handleFatalError(
                "Non-root Node should have a parent"
            )
        }

    val ancestryInfo: AncestryInfo =
        buildContext.ancestryInfo

    val tag: String =
        this::class.java.name

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

    val plugins: List<Plugin> =
        buildContext.defaultPlugins(this) + RIBs.globalPlugins + plugins + if (this is Plugin) listOf(
            this
        ) else emptyList()

    internal open val activationMode: ActivationMode =
        buildContext.activationMode

    internal val externalLifecycleRegistry = LifecycleRegistry(this)

    @VisibleForTesting
    internal val _children: MutableList<Node<*>> = mutableListOf()
    val children: List<Node<*>> get() = _children

    internal open val lifecycleManager = LifecycleManager(this)

    internal val isViewless: Boolean =
        viewFactory == null

    internal open var view: V? = null
    private var rootHost: RibView? = null

    internal open var savedViewState: Bundle? =
        buildParams.savedInstanceState?.getBundle(KEY_VIEW_STATE)

    internal var isAttachedToView: Boolean = false
        private set

    private var isPendingViewDetach: Boolean = false
    private var isPendingDetach: Boolean = false
    val isActive: Boolean
        get() = isAttachedToView && !isPendingViewDetach && !isPendingDetach

    init {
        verifyMainThread { "init" }
        this.plugins.filterIsInstance<NodeAware>().forEach { it.init(this) }
    }

    internal fun onBuildFinished() {
        verifyMainThread { "onBuildFinished" }
        plugins.filterIsInstance<NodeLifecycleAware>().forEach { it.onBuild() }
        plugins.filterIsInstance<GlobalNodeLifecycleAware>().forEach { it.onBuild(this) }
        parent?.onChildBuilt(this)
    }

    private fun onChildBuilt(child: Node<*>) {
        verifyMainThread { "onChildBuilt" }
        plugins.filterIsInstance<SubtreeChangeAware>().forEach { it.onChildBuilt(child) }
    }

    @CallSuper
    open fun onCreate() {
        verifyMainThread { "onCreate" }
        plugins
            .filterIsInstance<NodeLifecycleAware>()
            .forEach { it.onCreate(lifecycleManager.ribLifecycle.lifecycle) }
        plugins
            .filterIsInstance<GlobalNodeLifecycleAware>()
            .forEach { it.onCreate(this, lifecycleManager.ribLifecycle.lifecycle) }
        lifecycleManager.onCreate()
    }

    fun onCreateView(parentView: RibView): V? {
        verifyMainThread { "onCreateView" }
        if (isRoot) rootHost = parentView
        if (view == null && viewFactory != null) {
            val viewLifecycle = lifecycleManager.initializeViewLifecycle()
            val view = viewFactory.invoke(
                ViewFactory.Context(
                    parent = parentView,
                    lifecycle = viewLifecycle
                )
            )
            view.restoreInstanceState(savedViewState)
            savedViewState = null
            this.view = view
            lifecycleManager.onViewCreated()
            plugins.filterIsInstance<ViewAware<V>>().forEach {
                it.onViewCreated(view, viewLifecycle)
            }
        }

        return view
    }

    fun onAttachToView() {
        verifyMainThread { "onAttachToView" }
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

    fun onDetachFromView(): V? {
        verifyMainThread { "onDetachFromView" }
        if (isAttachedToView) {
            val view = view
            saveViewState()
            plugins.filterIsInstance<ViewLifecycleAware>().forEach { it.onDetachFromView() }
            lifecycleManager.onDetachFromView()
            isAttachedToView = false
            isPendingViewDetach = false
            rootHost = null
            this.view = null
            return view
        }
        return null
    }

    open fun onDestroy(isRecreating: Boolean) {
        verifyMainThread { "onDestroy" }
        if (view != null) {
            RIBs.errorHandler.handleNonFatalError(
                "View was not detached before node detach!",
                RuntimeException("View was not detached before node detach! RIB: $this")
            )
        }

        lifecycleManager.onDestroy()
        plugins.filterIsInstance<NodeLifecycleAware>().forEach { it.onDestroy() }
        plugins.filterIsInstance<GlobalNodeLifecycleAware>().forEach { it.onDestroy(this) }
        if (!isRecreating) {
            retainedInstanceStore.removeAll(identifier)
        }

        for (child in children.toList()) {
            detachChildNode(child, isRecreating)
        }

        isPendingDetach = false
    }

    /**
     * Attaches a child node to this node.
     *
     * @param child the [Node] to be attached.
     */
    @MainThread
    fun attachChildNode(child: Node<*>) {
        verifyMainThread { "attachChildNode" }
        verifyNotRoot(child)
        _children.add(child)
        lifecycleManager.onAttachChild(child)
        child.onCreate()
        onAttachChildNode(child)
        plugins.filterIsInstance<SubtreeChangeAware>().forEach { it.onChildAttached(child) }
        child.onAttachFinished()
    }

    internal fun onAttachFinished() {
        verifyMainThread { "onAttachFinished" }
        plugins.filterIsInstance<NodeLifecycleAware>().forEach { it.onAttach() }
        plugins.filterIsInstance<GlobalNodeLifecycleAware>().forEach { it.onAttach(this) }
    }

    open fun onAttachChildNode(child: Node<*>) {
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
        verifyMainThread { "attachChildView" }
        attachChildView(child, child, true)
    }

    private fun attachChildView(child: Node<*>, subtreeOf: Node<*>, notifyPlugins: Boolean) {
        if (isAttachedToView) {
            view?.attachChild(child, subtreeOf)
                ?: parent?.attachChildView(child, this, false)
                ?: integrationPoint.rootViewHost?.attachChild(child, this)
                ?: RIBs.errorHandler.handleFatalError(
                    "No view, no parent, and no root host should be technically impossible"
                )

            if (notifyPlugins) plugins.filterIsInstance<SubtreeViewChangeAware>()
                .forEach { it.onChildViewAttached(child) }
        }
    }

    fun detachChildView(child: Node<*>) {
        verifyMainThread { "detachChildView" }
        detachChildView(child, child, true)
    }

    private fun detachChildView(child: Node<*>, subtreeOf: Node<*>, notifyPlugins: Boolean) {
        if (isAttachedToView && child.isAttachedToView) {
            view?.detachChild(child, subtreeOf)
                ?: parent?.detachChildView(child, this, false)
                ?: rootHost?.detachChild(child, this)
                ?: RIBs.errorHandler.handleFatalError(
                    "No view, no parent, and no root host should be technically impossible"
                )

            if (notifyPlugins) plugins.filterIsInstance<SubtreeViewChangeAware>()
                .forEach { it.onChildViewDetached(child) }
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
    fun detachChildNode(child: Node<*>, isRecreating: Boolean) {
        verifyMainThread { "detachChildNode" }
        plugins.filterIsInstance<SubtreeChangeAware>().forEach { it.onChildDetached(child) }
        _children.remove(child)
        child.onDestroy(isRecreating)
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
        verifyMainThread { "onStart" }
        lifecycleManager.onStartExternal()
        plugins.filterIsInstance<AndroidLifecycleAware>().forEach { it.onStart() }
    }

    /**
     * To be called from the hosting environment (Activity, Fragment, etc.)
     */
    fun onStop() {
        verifyMainThread { "onStop" }
        lifecycleManager.onStopExternal()
        plugins.filterIsInstance<AndroidLifecycleAware>().forEach { it.onStop() }
    }

    /**
     * To be called from the hosting environment (Activity, Fragment, etc.)
     */
    fun onResume() {
        verifyMainThread { "onResume" }
        lifecycleManager.onResumeExternal()
        plugins.filterIsInstance<AndroidLifecycleAware>().forEach { it.onResume() }
    }

    /**
     * To be called from the hosting environment (Activity, Fragment, etc.)
     */
    fun onPause() {
        verifyMainThread { "onPause" }
        lifecycleManager.onPauseExternal()
        plugins.filterIsInstance<AndroidLifecycleAware>().forEach { it.onPause() }
    }

    @CallSuper
    open fun handleBackPress(): Boolean {
        verifyMainThread { "handleBackPress" }
        val subtreeHandlers = plugins.filterIsInstance<SubtreeBackPressHandler>()
        val handlers = plugins.filterIsInstance<BackPressHandler>()

        return subtreeHandlers.any { it.handleBackPressFirst() }
                || delegateHandleBackPressToActiveChildren()
                || handlers.any { it.handleBackPress() }
                || subtreeHandlers.any { it.handleBackPressFallback() }
    }

    fun upNavigation() {
        verifyMainThread { "upNavigation" }
        when {
            isRoot -> integrationPoint.handleUpNavigation()

            else -> parent?.handleUpNavigation()
                ?: RIBs.errorHandler.handleNonFatalError(
                    "Can't handle up navigation, Node is not a root and has no parent"
                )
        }
    }

    private fun handleUpNavigation() {
        val subtreeHandlers = plugins.filterIsInstance<UpNavigationHandler>()

        if (subtreeHandlers.none { it.handleUpNavigation() }) {
            upNavigation()
        }
    }

    private fun delegateHandleBackPressToActiveChildren(): Boolean =
        children
            .filter { it.isActive }
            .any { it.handleBackPress() }

    open fun onSaveInstanceState(outState: Bundle) {
        verifyMainThread { "onSaveInstanceState" }
        outState.putSerializable(Identifier.KEY_UUID, identifier.uuid)
        plugins.filterIsInstance<SavesInstanceState>().forEach { it.onSaveInstanceState(outState) }
        saveViewState()
        outState.putBundle(KEY_VIEW_STATE, savedViewState)
    }

    fun saveViewState() {
        verifyMainThread { "saveViewState" }
        val view = view ?: return
        savedViewState = Bundle().also { view.saveInstanceState(it) }
    }

    fun onLowMemory() {
        verifyMainThread { "onLowMemory" }
        plugins.filterIsInstance<SystemAware>().forEach { it.onLowMemory() }
    }

    override val lifecycle: Lifecycle
        get() = lifecycleManager.lifecycle

    fun getView(): V? = view

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

    inline fun <reified P> pluginRoot(): P? {
        var found: P? = null
        var current: Node<*>? = this

        while (current != null) {
            val plugin = current.plugin<P>()
            if (plugin != null) found = plugin
            current = current.parent
        }

        return found
    }

    private inline fun verifyMainThread(functionName: () -> String) {
        if (!isOnMainThreadFunc()) {
            RIBs.errorHandler.handleNonFatalError(
                "Node interacted with outside main thread. " +
                        "Node: ${javaClass.name}, " +
                        "method: ${functionName()}, " +
                        "thread: ${Thread.currentThread().name}"
            )
        }
    }
}
