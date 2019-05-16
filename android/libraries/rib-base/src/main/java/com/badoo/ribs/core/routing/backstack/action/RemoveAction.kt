package com.badoo.ribs.core.routing.backstack.action

import android.os.Parcelable
import com.badoo.ribs.core.Node
import com.badoo.ribs.core.routing.backstack.ConfigurationContext.Resolved

/**
 * Removes [Node]s from their parent, resulting in the end of their lifecycles.
 */
internal object RemoveAction : ResolvedSingleConfigurationAction() {

    override fun <C : Parcelable> execute(item: Resolved<C>, params: ActionExecutionParams<C>): Resolved<C> {
        val (_, parentNode, _) = params
        item.nodes.forEach {
            parentNode.detachChildView(it.node)
            parentNode.detachChildNode(it.node)
        }

        return item
    }
}
