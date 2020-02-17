package com.badoo.ribs.core.routing.action

import com.badoo.ribs.core.Node
import com.badoo.ribs.core.builder.NodeFactory
import com.badoo.ribs.core.view.RibView

class AnchoredAttachRoutingAction<V : RibView>(
    private val anchor: Node<*>,
    nodeFactory: NodeFactory
) : AttachRibRoutingAction<V>(
    nodeFactory = nodeFactory
) {

    override fun anchor(): Node<*>? =
        anchor

    companion object {
        fun <V : RibView> anchor(anchor: Node<*>, nodeFactory: NodeFactory): RoutingAction<V> =
                AnchoredAttachRoutingAction(
                    anchor = anchor,
                    nodeFactory = nodeFactory
                )
    }
}
