package com.badoo.ribs.core.routing.backstack

import android.os.Bundle
import android.os.Parcelable
import com.badoo.mvicore.binder.Binder
import com.badoo.ribs.core.Node
import com.badoo.ribs.core.Node.ViewAttachMode.PARENT
import com.badoo.ribs.core.routing.action.RoutingAction
import io.reactivex.disposables.Disposable

internal class ConnectorCommandExecutor<C : Parcelable> private constructor(
    private val binder: Binder,
    private val resolver: (C) -> RoutingAction<*>,
    private val parentNode: Node<*>
) : Disposable by binder {

    constructor(
        resolver: (C) -> RoutingAction<*>,
        parentNode: Node<*>
    ) : this(
        binder = Binder(),
        resolver = resolver,
        parentNode = parentNode
    )

    // FIXME persist this to Bundle?
    internal val nodes: MutableList<BackStackElement<C>> = mutableListOf()

    internal fun addConfiguration(index: Int, configuration: C) {
        if (index <= nodes.lastIndex) {
            // TODO issue warning for accidental override
        }

        nodes.add(index, BackStackElement(configuration).apply { init() })
    }

    private fun BackStackElement<C>.init() {
        if (routingAction == null) {
            routingAction = resolver.invoke(configuration)
        }
        if (builtNodes == null) {
            buildNodes()
            attachBuiltNodes()
        }
    }

    private fun BackStackElement<C>.buildNodes() {
        builtNodes = routingAction!!.buildNodes()
    }

    private fun BackStackElement<C>.attachBuiltNodes() {
        builtNodes!!.forEachIndexed { index, nodeDescriptor ->
            parentNode.attachChildNode(nodeDescriptor.node, bundleAt(index))
        }
    }

    private fun BackStackElement<C>.bundleAt(index: Int): Bundle? =
        bundles.elementAtOrNull(index)?.also {
            it.classLoader = BackStackManager.State::class.java.classLoader
        }

    internal fun makeConfigurationActive(index: Int) {
        nodes[index].run {
            init()
            reAttachViewsIfNeeded()
            routingAction?.execute()
        }
    }

    private fun BackStackElement<C>.reAttachViewsIfNeeded() {
        builtNodes!!
            .forEach {
                if (it.viewAttachMode == PARENT && !it.node.isViewAttached) {
                    parentNode.attachChildView(it.node)
                }
            }
    }

    internal fun makeConfigurationPassive(index: Int) {
        nodes[index].run {
            routingAction?.cleanup()
            saveAndDetachChildViews()
        }
    }

    private fun BackStackElement<C>.saveAndDetachChildViews() {
        builtNodes?.forEach {
            it.node.saveViewState()

            if (it.viewAttachMode == PARENT) {
                parentNode.detachChildView(it.node)
            }
        }
    }

    internal fun removeConfiguration(index: Int) {
        nodes[index].run {
            destroyChildren()
        }
        nodes.removeAt(index)
    }

    private fun BackStackElement<C>.destroyChildren() {
        builtNodes?.forEach {
            parentNode.detachChildView(it.node)
            parentNode.detachChildNode(it.node)
        }
        builtNodes = null
    }

//    // FIXME this can be done with local [nodes] field
//    fun shrinkToBundles(backStack: List<BackStackElement<C>>): List<BackStackElement<C>> =
//        saveInstanceState(backStack).apply {
//            dropLast(1).forEach {
//                it.builtNodes?.forEach {
//                    parentNode.detachChildView(it.node)
//                    parentNode.detachChildNode(it.node)
//                }
//                it.builtNodes = null
//            }
//        }
//
//    // FIXME this can be done with local [nodes] field
//    fun saveInstanceState(backStack: List<BackStackElement<C>>): List<BackStackElement<C>> {
//        backStack.forEach {
//            it.bundles = it.builtNodes?.map { nodeDescriptor ->
//                Bundle().also {
//                    nodeDescriptor.node.onSaveInstanceState(it)
//                }
//            } ?: emptyList()
//        }
//
//        return backStack
//    }

//    fun detachFromView() {
//        permanentParts.forEach { parentNode.detachChildView(it) }
//
//        // FIXME +contents below overlays in last position
//        makeConfigurationPassive(backStackManager.state.backStack.lastIndex)
//    }
//
//    fun attachToView() {
//        permanentParts.forEach { parentNode.attachChildView(it) }
//
//        // FIXME +contents below overlays in last position
//        makeConfigurationActive(backStackManager.state.backStack.lastIndex)
//    }
}
