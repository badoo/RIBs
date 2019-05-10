package com.badoo.ribs.core.routing.backstack

import android.os.Bundle
import android.os.Parcelable
import com.badoo.mvicore.binder.Binder
import com.badoo.ribs.core.Node
import com.badoo.ribs.core.Node.ViewAttachMode.PARENT
import com.badoo.ribs.core.routing.NodeConnector
import com.badoo.ribs.core.routing.action.RoutingAction
import com.badoo.ribs.core.routing.backstack.BackStackRibConnector.DetachStrategy.DESTROY
import com.badoo.ribs.core.routing.backstack.BackStackRibConnector.DetachStrategy.DETACH_VIEW
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Consumer

internal class BackStackRibConnector<C : Parcelable> private constructor(
    private val binder: Binder,
    private val backStackManager: BackStackManager<C>,
    private val permanentParts: List<Node<*>>,
    private val resolver: (C) -> RoutingAction<*>,
    private val connector: NodeConnector
) : Disposable by binder {

    enum class DetachStrategy {
        DESTROY, DETACH_VIEW
    }

    constructor(
        backStackManager: BackStackManager<C>,
        permanentParts: List<Node<*>>,
        resolver: (C) -> RoutingAction<*>,
        connector: NodeConnector
    ) : this(
        binder = Binder(),
        backStackManager = backStackManager,
        permanentParts = permanentParts,
        resolver = resolver,
        connector = connector
    )

    // FIXME persist this to Bundle?
    private val nodes: MutableMap<Int, BackStackElement<C>> = mutableMapOf()

    private val onConfigurationChange: Consumer<BackStackManager.News<C>> = Consumer {
        when (it) {
            is BackStackManager.News.ConfigurationChange -> {
                it.toKill.forEach { index ->
                    // TODO make sure null check is not necessary and it works regardless
                    nodes[index]?.let {
                        leave(it, DESTROY)
                    }
                    nodes.remove(index)
                }

                it.toDetachView.forEach { index ->
                    // TODO make sure null check is not necessary and it works regardless
                    nodes[index]?.let {
                        leave(it, DETACH_VIEW)
                    }
                }

                it.toAttachView.forEach { (index, configuration) ->
                    // TODO where to check accidental override? not here, as in the case of revive after Pop it's valid that we already have an element
                    if (!nodes.containsKey(index)) {
                        nodes[index] = BackStackElement(configuration)
                    }
                    goTo(nodes[index]!!)
                }
            }
        }
    }

    init {
        permanentParts.forEach {
            connector.attachChildNode(it)
        }

        binder.bind(backStackManager.news to onConfigurationChange)
        backStackManager.state.backStack.forEachIndexed { index, configuration ->
            nodes[index] = BackStackElement(configuration)
        }
    }

    // TODO remove this, use destroyChildren() / saveAndDetachChildViews() directly
    fun leave(backStackElement: BackStackElement<C>, detachStrategy: DetachStrategy): BackStackElement<C> {
        with(backStackElement) {
            routingAction?.cleanup()

            when (detachStrategy) {
                DESTROY -> destroyChildren()
                DETACH_VIEW -> saveAndDetachChildViews()
            }
        }

        return backStackElement
    }

    private fun BackStackElement<C>.destroyChildren() {
        builtNodes?.forEach {
            connector.detachChildView(it.node)
            connector.detachChildNode(it.node)
        }
        builtNodes = null
    }

    private fun BackStackElement<C>.saveAndDetachChildViews() {
        builtNodes?.forEach {
            it.node.saveViewState()

            if (it.viewAttachMode == PARENT) {
                connector.detachChildView(it.node)
            }
        }
    }

    fun goTo(backStackElement: BackStackElement<C>): BackStackElement<C> {
        with(backStackElement) {
            if (routingAction == null) {
                routingAction = resolver.invoke(configuration)
            }

            if (builtNodes == null) {
                buildNodes()
                attachBuiltNodes()
            }

            reAttachViewsIfNeeded()
            routingAction!!.execute()
        }

        return backStackElement
    }

    private fun BackStackElement<C>.buildNodes() {
        builtNodes = routingAction!!.buildNodes()
    }

    private fun BackStackElement<C>.attachBuiltNodes() {
        builtNodes!!.forEachIndexed { index, nodeDescriptor ->
            connector.attachChildNode(nodeDescriptor.node, bundleAt(index))
        }
    }

    private fun BackStackElement<C>.reAttachViewsIfNeeded() {
        builtNodes!!
            .forEach {
                if (it.viewAttachMode == PARENT && !it.node.isViewAttached) {
                    connector.attachChildView(it.node)
                }
            }
    }

    private fun BackStackElement<C>.bundleAt(index: Int): Bundle? =
        bundles.elementAtOrNull(index)?.also {
            it.classLoader = BackStackManager.State::class.java.classLoader
        }

    // FIXME this can be done with local [nodes] field
    fun shrinkToBundles(backStack: List<BackStackElement<C>>): List<BackStackElement<C>> =
        saveInstanceState(backStack).apply {
            dropLast(1).forEach {
                it.builtNodes?.forEach {
                    connector.detachChildView(it.node)
                    connector.detachChildNode(it.node)
                }
                it.builtNodes = null
            }
        }

    // FIXME this can be done with local [nodes] field
    fun saveInstanceState(backStack: List<BackStackElement<C>>): List<BackStackElement<C>> {
        backStack.forEach {
            it.bundles = it.builtNodes?.map { nodeDescriptor ->
                Bundle().also {
                    nodeDescriptor.node.onSaveInstanceState(it)
                }
            } ?: emptyList()
        }

        return backStack
    }

    fun detachFromView() {
        permanentParts.forEach { connector.detachChildView(it) }

        // FIXME +contents below overlays in last position
        nodes[backStackManager.state.backStack.lastIndex]?.let {
            leave(it, DETACH_VIEW)
        }
    }

    fun attachToView() {
        permanentParts.forEach { connector.attachChildView(it) }

        // FIXME +contents below overlays in last position
        nodes[backStackManager.state.backStack.lastIndex]?.let {
            goTo(it)
        }
    }
}
