package com.badoo.ribs.core.routing.configuration.action.single

import android.os.Bundle
import android.os.Parcelable
import com.badoo.ribs.core.Node
import com.badoo.ribs.core.routing.configuration.ConfigurationContext
import com.badoo.ribs.core.routing.configuration.ConfigurationContext.Resolved
import com.badoo.ribs.core.routing.configuration.action.ActionExecutionParams

/**
 * Attaches [Node]s to a parentNode without their views
 */
internal object AddAction : ResolvedSingleConfigurationAction() {

    override fun <C : Parcelable> execute(item: Resolved<C>, params: ActionExecutionParams<C>): Resolved<C> {
        val (_, parentNode, _) = params
        return execute(item, parentNode)
    }

    /**
     * Convenience method so that Add can be called only with only the knowledge of parentNode too
     */
    fun <C : Parcelable> execute(item: Resolved<C>, parentNode: Node<*>): Resolved<C> {
        parentNode.attachNodes(item.nodes)

        return item
    }

    private fun Node<*>.attachNodes(nodes: List<Node.Descriptor>) {
        nodes.forEach {
            attachChildNode(it.node)
        }
    }

    private fun List<Bundle?>.bundleAt(index: Int): Bundle? =
        elementAtOrNull(index)?.also {
            it.classLoader = ConfigurationContext::class.java.classLoader
        }
}
