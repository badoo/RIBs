package com.badoo.ribs.core.routing.action

import com.badoo.ribs.core.Node
import com.badoo.ribs.core.builder.BuildContext

class CompositeRoutingAction(
    private vararg val routingActions: RoutingAction
) : RoutingAction {

    override val nbNodesToBuild: Int = routingActions.asList().fold(0) { acc, r -> acc + r.nbNodesToBuild }

    constructor(routingActions: List<RoutingAction>) : this(*routingActions.toTypedArray())

    override fun buildNodes(buildContexts: List<BuildContext>) : List<Node<*>> =
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
