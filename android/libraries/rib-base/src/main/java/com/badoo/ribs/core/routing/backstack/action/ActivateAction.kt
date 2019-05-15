package com.badoo.ribs.core.routing.backstack.action

import com.badoo.ribs.core.Node
import com.badoo.ribs.core.routing.action.RoutingAction
import com.badoo.ribs.core.routing.backstack.ConfigurationContext

/**
 * Attaches views of associated [Node]s to a parentNode, and executes the associated [RoutingAction].
 *
 * The [Node]s are expected to be already added to the parentNode on a logical level.
 */
internal object ActivateAction : SingleConfigurationAction {

    override fun execute(item: ConfigurationContext.Resolved<*>, parentNode: Node<*>) {
        parentNode.attachParentedViews(item.nodes)
        item.routingAction.execute()
    }

    private fun Node<*>.attachParentedViews(nodes: List<Node.Descriptor>) {
        nodes.forEach {
            if (it.viewAttachMode == Node.ViewAttachMode.PARENT && !it.node.isViewAttached) {
                attachChildView(it.node)
            }
        }
    }
}
