package com.badoo.ribs.routing.action

import com.badoo.ribs.core.Rib
import com.badoo.ribs.core.modality.BuildContext

class CompositeRoutingAction(
    private vararg val routingActions: RoutingAction
) : RoutingAction {

    override val numberOfNodes: Int = routingActions.fold(0) { acc, r -> acc + r.numberOfNodes }

    constructor(routingActions: List<RoutingAction>) : this(*routingActions.toTypedArray())

    override fun buildNodes(buildContexts: List<BuildContext>) : List<Rib> =
        routingActions.mapIndexed { index, routingAction ->
            routingAction.buildNodes(
                buildContexts = listOfNotNull(buildContexts.getOrNull(index))
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
