package com.badoo.ribs.core.routing.action

import com.badoo.ribs.core.Node
import com.badoo.ribs.core.builder.BuildContext
import com.badoo.ribs.core.view.RibView
import com.badoo.ribs.core.routing.portal.AncestryInfo

class CompositeRoutingAction(
    private vararg val routingActions: RoutingAction
) : RoutingAction {

    constructor(routingActions: List<RoutingAction>) : this(*routingActions.toTypedArray())

    override fun buildNodes(buildContexts: List<BuildContext>) : List<Node<*>> =
        routingActions.mapIndexed { index, routingAction ->
            routingAction.buildNodes(
                buildContexts = buildContexts.getOrNull(index)?.let { listOf(it) } ?: emptyList()
            )
        }.flatten()

    override fun execute() {
        routingActions.forEach {
            it.execute()
        }
    }

    override fun cleanup() {
        routingActions.forEach {
            it.cleanup()
        }
    }

    companion object {
        fun composite(vararg routingActions: RoutingAction): RoutingAction =
            CompositeRoutingAction(*routingActions)
    }
}
