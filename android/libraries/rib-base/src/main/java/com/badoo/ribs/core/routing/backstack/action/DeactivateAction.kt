package com.badoo.ribs.core.routing.backstack.action

import com.badoo.ribs.core.Node
import com.badoo.ribs.core.routing.backstack.ConfigurationContext

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
