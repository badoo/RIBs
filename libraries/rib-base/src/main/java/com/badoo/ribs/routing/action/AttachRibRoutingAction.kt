package com.badoo.ribs.routing.action

import com.badoo.ribs.core.Rib
import com.badoo.ribs.core.builder.BuildContext
import com.badoo.ribs.core.builder.NodeFactory

open class AttachRibRoutingAction(
    private val nodeFactory: NodeFactory
) : RoutingAction {

    override val nbNodesToBuild: Int = 1

    override fun buildNodes(buildContexts: List<BuildContext>): List<Rib> =
        listOf(
            nodeFactory.invoke(
                buildContexts.first()
            )
        )

    companion object {
        fun attach(nodeFactory: NodeFactory): RoutingAction =
            AttachRibRoutingAction(nodeFactory)
    }
}
