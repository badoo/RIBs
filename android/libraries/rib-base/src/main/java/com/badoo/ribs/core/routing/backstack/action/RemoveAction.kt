package com.badoo.ribs.core.routing.backstack.action

import com.badoo.ribs.core.Node
import com.badoo.ribs.core.routing.backstack.ConfigurationContext

internal object RemoveAction : SingleConfigurationAction {

    override fun execute(item: ConfigurationContext.Resolved<*>, parentNode: Node<*>) {
        item.nodes.forEach {
            parentNode.detachChildView(it.node)
            parentNode.detachChildNode(it.node)
        }
    }
}
