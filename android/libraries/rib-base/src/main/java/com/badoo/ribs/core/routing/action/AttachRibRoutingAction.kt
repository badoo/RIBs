package com.badoo.ribs.core.routing.action

import android.os.Bundle
import com.badoo.ribs.core.AttachMode
import com.badoo.ribs.core.builder.BuildContext
import com.badoo.ribs.core.Node
import com.badoo.ribs.core.builder.NodeFactory
import com.badoo.ribs.core.routing.portal.AncestryInfo
import com.badoo.ribs.core.view.RibView

open class AttachRibRoutingAction<V : RibView>(
    private val nodeFactory: NodeFactory
) : RoutingAction<V> {

    override fun buildNodes(ancestryInfo: AncestryInfo, bundles: List<Bundle?>): List<Node<*>> =
        listOf(
            nodeFactory.invoke(
                BuildContext(
                    ancestryInfo = ancestryInfo,
                    viewAttachMode = AttachMode.PARENT,
                    savedInstanceState = bundles.firstOrNull()
                )
            )
        )

    companion object {
        fun <V : RibView> attach(nodeFactory: NodeFactory): RoutingAction<V> =
            AttachRibRoutingAction(nodeFactory)
    }
}
