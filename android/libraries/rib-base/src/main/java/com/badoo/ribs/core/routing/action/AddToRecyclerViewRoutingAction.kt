package com.badoo.ribs.core.routing.action

import com.badoo.ribs.core.AttachMode
import com.badoo.ribs.core.Rib
import com.badoo.ribs.core.builder.BuildContext
import com.badoo.ribs.core.builder.NodeFactory

open class AddToRecyclerViewRoutingAction(
    private val nodeFactory: NodeFactory
) : RoutingAction {

    override val nbNodesToBuild: Int = 1

    override fun buildNodes(buildContexts: List<BuildContext>): List<Rib<*>> =
        listOf(
            nodeFactory.invoke(
                buildContexts.first().copy(
                    attachMode = AttachMode.EXTERNAL
                )
            )
        )

    companion object {
        fun recyclerView(nodeFactory: NodeFactory): RoutingAction =
            AddToRecyclerViewRoutingAction(nodeFactory)
    }
}
