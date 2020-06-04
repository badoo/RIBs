package com.badoo.ribs.android.recyclerview.routing.action

import com.badoo.ribs.core.ActivationMode
import com.badoo.ribs.core.Rib
import com.badoo.ribs.core.builder.BuildContext
import com.badoo.ribs.core.builder.RibFactory
import com.badoo.ribs.routing.action.RoutingAction

open class AddToRecyclerViewRoutingAction(
    private val ribFactory: RibFactory
) : RoutingAction {

    override val nbNodesToBuild: Int = 1

    override fun buildNodes(buildContexts: List<BuildContext>): List<Rib> =
        listOf(
            ribFactory.invoke(
                buildContexts.first().copy(
                    activationMode = ActivationMode.CLIENT
                )
            )
        )

    companion object {
        fun recyclerView(ribFactory: RibFactory): RoutingAction =
            AddToRecyclerViewRoutingAction(
                ribFactory
            )
    }
}
