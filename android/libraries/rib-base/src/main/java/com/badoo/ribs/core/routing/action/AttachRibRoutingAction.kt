package com.badoo.ribs.core.routing.action

import android.os.Bundle
import com.badoo.ribs.core.AttachMode
import com.badoo.ribs.core.Node
import com.badoo.ribs.core.builder.BuildContext
import com.badoo.ribs.core.builder.NodeFactory
import com.badoo.ribs.core.routing.portal.AncestryInfo

open class AttachRibRoutingAction(
    private val nodeFactory: NodeFactory
) : RoutingAction {

    override fun buildNodes(ancestryInfo: AncestryInfo, bundles: List<Bundle?>): List<Node<*>> =
        listOf(
            nodeFactory.invoke(
                BuildContext(
                    ancestryInfo = ancestryInfo,
                    attachMode = AttachMode.PARENT,
                    savedInstanceState = bundles.firstOrNull()
                )
            )
        )

    companion object {
        fun attach(nodeFactory: NodeFactory): RoutingAction =
            AttachRibRoutingAction(nodeFactory)
    }
}
