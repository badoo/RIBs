package com.badoo.ribs.core.routing.backstack.action

import com.badoo.ribs.core.Node
import com.badoo.ribs.core.routing.backstack.ConfigurationContext

/**
 * Removes [Node]s from their parent, resulting in the end of their lifecycles.
 */
internal object RemoveAction : SingleConfigurationAction {

    override fun execute(item: ConfigurationContext.Resolved<*>, parentNode: Node<*>) {
        item.nodes.forEach {
            parentNode.detachChildView(it.node)
            parentNode.detachChildNode(it.node)
        }
    }
}
