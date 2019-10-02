package com.badoo.ribs.core.routing.action

import android.os.Bundle
import com.badoo.ribs.core.Node
import com.badoo.ribs.core.routing.portal.AncestryInfo
import com.badoo.ribs.core.view.RibView

class CompositeRoutingAction< V : RibView>(
    private vararg val routingActions: RoutingAction<V>
) : RoutingAction<V> {

    constructor(routingActions: List<RoutingAction<V>>) : this(*routingActions.toTypedArray())

    override fun buildNodes(ancestryInfo: AncestryInfo, bundles: List<Bundle?>) : List<Node.Descriptor> =
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
        fun < V : RibView> composite(vararg routingActions: RoutingAction<V>): RoutingAction<V> =
            CompositeRoutingAction(*routingActions)
    }
}
