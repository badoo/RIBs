package com.badoo.ribs.core.routing.configuration.action.single

import android.os.Parcelable
import com.badoo.ribs.core.Node
import com.badoo.ribs.core.routing.action.RoutingAction
import com.badoo.ribs.core.routing.configuration.ConfigurationContext.ActivationState.INACTIVE
import com.badoo.ribs.core.routing.configuration.ConfigurationContext.Resolved
import com.badoo.ribs.core.routing.configuration.action.ActionExecutionParams

/**
 * Detaches views of associated [Node]s to a parentNode, and cleans up the associated [RoutingAction].
 *
 * Will not detach the [Node]s on the logical level, they are kept alive without their views.
 */
internal object DeactivateAction : ResolvedSingleConfigurationAction() {

    override fun <C : Parcelable> execute(item: Resolved<C>, params: ActionExecutionParams<C>): Resolved<C> {
        val (_, parentNode, _) = params
        item.routingAction.cleanup(item.nodes)
        item.nodes.saveViewState()
//        val targetNode = item.routingAction.parentNode() ?: parentNode
        parentNode.detachChildViews(item.nodes)

        return item.withActivationState(INACTIVE)
    }

    private fun List<Node.Descriptor>.saveViewState() {
        forEach {
            it.node.saveViewState()
        }
    }

    private fun Node<*>.detachChildViews(nodes: List<Node.Descriptor>) {
        nodes.forEach {
            if (it.viewAttachMode == Node.AttachMode.PARENT) {
                detachChildView(it.node)
            }
        }
    }
}
