package com.badoo.ribs.routing.action

import com.badoo.ribs.core.Rib
import com.badoo.ribs.core.modality.BuildContext

open class AttachRibRoutingAction(
    private val ribFactory: RibFactory
) : RoutingAction {

    override val nbNodesToBuild: Int = 1

    override fun buildNodes(buildContexts: List<BuildContext>): List<Rib> =
        listOf(
            ribFactory.invoke(
                buildContexts.first()
            )
        )

    companion object {
        fun attach(ribFactory: RibFactory): RoutingAction =
            AttachRibRoutingAction(ribFactory)
    }
}
