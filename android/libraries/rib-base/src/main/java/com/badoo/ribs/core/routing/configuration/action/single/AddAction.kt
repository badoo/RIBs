package com.badoo.ribs.core.routing.configuration.action.single

import android.os.Parcelable
import com.badoo.ribs.core.Node
import com.badoo.ribs.core.routing.configuration.ConfigurationContext.Resolved
import com.badoo.ribs.core.routing.transition.TransitionElement

/**
 * Attaches [Node]s to a parentNode without their views
 */
internal class AddAction<C : Parcelable>(
    private var item: Resolved<C>,
    private val parentNode: Node<*>
) : ReversibleAction<C>() {

    override fun onBeforeTransition() {
    }

    override fun onTransition() {
        if (isReversed) {
            parentNode.detachNodes(item.nodes)
        } else {
            parentNode.attachNodes(item.nodes)
        }
    }

    private fun Node<*>.attachNodes(nodes: List<Node<*>>) {
        nodes.forEach {
            attachChildNode(it)
        }
    }

    private fun Node<*>.detachNodes(nodes: List<Node.Descriptor>) {
        nodes.forEach {
            detachChildNode(it.node)
        }
    }

    override fun onFinish() {
    }

    override val result: Resolved<C> =
        item

    override val transitionElements: List<TransitionElement<C>> =
        emptyList()
}
