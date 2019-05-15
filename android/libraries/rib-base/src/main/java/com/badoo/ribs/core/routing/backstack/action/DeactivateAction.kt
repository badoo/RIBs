package com.badoo.ribs.core.routing.backstack.action

import com.badoo.ribs.core.Node
import com.badoo.ribs.core.routing.action.RoutingAction
import com.badoo.ribs.core.routing.backstack.ConfigurationContext

/**
 * Detaches views of associated [Node]s to a parentNode, and cleans up the associated [RoutingAction].
 *
 * Will not detach the [Node]s on the logical level, they are kept alive without their views.
 */
internal object DeactivateAction : SingleConfigurationAction {

    override fun execute(item: ConfigurationContext.Resolved<*>, parentNode: Node<*>) {
        item.routingAction.cleanup()
        item.nodes.saveViewState()
        parentNode.detachChildViews(item.nodes)
    }

    private fun List<Node.Descriptor>.saveViewState() {
        forEach {
            it.node.saveViewState()
        }
    }

    private fun Node<*>.detachChildViews(nodes: List<Node.Descriptor>) {
        nodes.forEach {
            if (it.viewAttachMode == Node.ViewAttachMode.PARENT) {
                detachChildView(it.node)
            }
        }
    }
}
