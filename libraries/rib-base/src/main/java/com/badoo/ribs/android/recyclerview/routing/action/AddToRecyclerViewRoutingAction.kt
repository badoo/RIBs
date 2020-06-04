package com.badoo.ribs.android.recyclerview.routing.action

import com.badoo.ribs.core.ActivationMode
import com.badoo.ribs.core.Rib
import com.badoo.ribs.core.builder.BuildContext
import com.badoo.ribs.core.builder.NodeFactory
import com.badoo.ribs.core.routing.action.RoutingAction

open class AddToRecyclerViewRoutingAction(
    private val nodeFactory: NodeFactory
) : RoutingAction {

    override val nbNodesToBuild: Int = 1

    override fun buildNodes(buildContexts: List<BuildContext>): List<Rib> =
        listOf(
            nodeFactory.invoke(
                buildContexts.first().copy(
                    activationMode = ActivationMode.CLIENT
                )
            )
        )

    companion object {
        fun recyclerView(nodeFactory: NodeFactory): RoutingAction =
            AddToRecyclerViewRoutingAction(
                nodeFactory
            )
    }
}
