package com.badoo.ribs.core.routing.backstack.action

import com.badoo.ribs.core.Node
import com.badoo.ribs.core.routing.backstack.ConfigurationContext

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
