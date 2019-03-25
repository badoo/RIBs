package com.badoo.ribs.core.routing.backstack

import android.os.Bundle
import android.os.Parcelable
import com.badoo.ribs.core.Node
import com.badoo.ribs.core.Node.ViewAttachMode.PARENT
import com.badoo.ribs.core.routing.NodeConnector
import com.badoo.ribs.core.routing.action.RoutingAction
import com.badoo.ribs.core.routing.backstack.BackStackRibConnector.DetachStrategy.DESTROY
import com.badoo.ribs.core.routing.backstack.BackStackRibConnector.DetachStrategy.DETACH_VIEW

internal class BackStackRibConnector<C : Parcelable>(
    private val permanentParts: List<Node<*>>,
    private val resolver: (C) -> RoutingAction<*>,
    private val connector: NodeConnector
) {
    enum class DetachStrategy {
        DESTROY, DETACH_VIEW
    }

    init {
        permanentParts.forEach {
            connector.attachChildNode(it)
        }
    }

    fun leave(backStackElement: BackStackElement<C>, detachStrategy: DetachStrategy): BackStackElement<C> {
        with(backStackElement) {
            routingAction?.cleanup()

            when (detachStrategy) {
                DESTROY -> destroyRibs()
                DETACH_VIEW -> saveAndDetachView()
            }
        }

        return backStackElement
    }

    private fun BackStackElement<C>.destroyRibs() {
        builtNodes?.forEach {
            connector.detachChildView(it.node)
            connector.detachChildNode(it.node)
        }
        builtNodes = null
    }

    private fun BackStackElement<C>.saveAndDetachView(): Unit? {
        return builtNodes?.forEach {
            val (node, viewAttachMode) = it
            node.saveViewState()

            if (viewAttachMode == PARENT) {
                connector.detachChildView(node)
            }
        }
    }

    fun goTo(backStackElement: BackStackElement<C>): BackStackElement<C> {
        with(backStackElement) {
            if (routingAction == null) {
                routingAction = resolver.invoke(configuration)
            }

            if (builtNodes == null) {
                createRibs()
                attachRibs()
            } else {
                reAttachRibViews()
            }

            routingAction!!.execute()
        }

        return backStackElement
    }

    private fun BackStackElement<C>.createRibs() {
        builtNodes = routingAction!!.buildNodes()

    }

    private fun BackStackElement<C>.attachRibs() {
        builtNodes!!.forEachIndexed { index, nodeDescriptor ->
            connector.attachChildNode(nodeDescriptor.node, bundleAt(index))

            if (nodeDescriptor.viewAttachMode == PARENT) {
                connector.attachChildView(nodeDescriptor.node)
            }
        }
    }

    private fun BackStackElement<C>.reAttachRibViews() {
        builtNodes!!
            .forEach {
                val (node, viewAttachMode) = it
                if (viewAttachMode == PARENT) {
                    connector.attachChildView(node)
                }
            }
    }

    private fun BackStackElement<C>.bundleAt(index: Int): Bundle? =
        bundles.elementAtOrNull(index)?.also {
            it.classLoader = BackStackManager.State::class.java.classLoader
        }

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

    fun detachFromView(backStack: List<BackStackElement<C>>) {
        permanentParts.forEach { connector.detachChildView(it) }

        backStack.lastOrNull()?.let {
            leave(it, DETACH_VIEW)
        }
    }

    fun attachToView(backStack: List<BackStackElement<C>>) {
        permanentParts.forEach { connector.attachChildView(it) }

        backStack.lastOrNull()?.let {
            goTo(it)
        }
    }
}
