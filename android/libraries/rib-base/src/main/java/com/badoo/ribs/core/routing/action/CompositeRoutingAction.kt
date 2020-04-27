package com.badoo.ribs.core.routing.action

import android.os.Bundle
import com.badoo.ribs.core.Node
import com.badoo.ribs.core.routing.portal.AncestryInfo

class CompositeRoutingAction(
    private vararg val routingActions: RoutingAction
) : RoutingAction {

    constructor(routingActions: List<RoutingAction>) : this(*routingActions.toTypedArray())

    override fun buildNodes(ancestryInfo: AncestryInfo, bundles: List<Bundle?>) : List<Node<*>> =
        routingActions.mapIndexed { index, routingAction ->
            routingAction.buildNodes(
                ancestryInfo = ancestryInfo,
                bundles = bundles.getOrNull(index)?.let { listOf(it) } ?: emptyList()
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
