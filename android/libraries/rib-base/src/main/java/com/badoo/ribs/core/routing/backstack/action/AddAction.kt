package com.badoo.ribs.core.routing.backstack.action

import android.os.Bundle
import com.badoo.ribs.core.Node
import com.badoo.ribs.core.routing.backstack.ConfigurationContext

/**
 * Attaches [Node]s to a parentNode without their views
 */
internal object AddAction : SingleConfigurationAction {

    override fun execute(item: ConfigurationContext.Resolved<*>, parentNode: Node<*>) {
        parentNode.attachNodes(item.nodes, item.bundles)
    }

    private fun Node<*>.attachNodes(nodes: List<Node.Descriptor>, bundles: List<Bundle?>) {
        nodes.forEachIndexed { index, nodeDescriptor ->
            attachChildNode(nodeDescriptor.node, bundles.bundleAt(index))
        }
    }

    private fun List<Bundle?>.bundleAt(index: Int): Bundle? =
        elementAtOrNull(index)?.also {
            it.classLoader = ConfigurationContext::class.java.classLoader
        }
}
