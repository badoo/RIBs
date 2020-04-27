package com.badoo.ribs.core.routing.action

import android.os.Bundle
import com.badoo.ribs.core.AttachMode
import com.badoo.ribs.core.Node
import com.badoo.ribs.core.builder.BuildContext
import com.badoo.ribs.core.builder.NodeFactory
import com.badoo.ribs.core.routing.portal.AncestryInfo
import com.badoo.ribs.core.view.RibView
import com.badoo.ribs.customisation.RibCustomisationDirectory

open class AttachRibRoutingAction(
    private val nodeFactory: NodeFactory
) : RoutingAction {

    override fun buildNodes(
        buildContexts: List<BuildContext>
    ): List<Node<*>> =
        listOf(
            nodeFactory.invoke(
                buildContext
//                    (
//                    ancestryInfo = ancestryInfo,
//                    attachMode = AttachMode.PARENT,
//                    savedInstanceState = bundles.firstOrNull(),
//                    customisations = customisations
//                )
            )
        )

    companion object {
        fun attach(nodeFactory: NodeFactory): RoutingAction =
            AttachRibRoutingAction(nodeFactory)
    }
}
