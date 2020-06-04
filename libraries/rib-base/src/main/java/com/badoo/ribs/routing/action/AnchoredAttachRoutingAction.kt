package com.badoo.ribs.routing.action

import com.badoo.ribs.core.Node
import com.badoo.ribs.core.builder.NodeFactory

class AnchoredAttachRoutingAction(
    private val anchor: Node<*>,
    nodeFactory: NodeFactory
) : AttachRibRoutingAction(
    nodeFactory = nodeFactory
) {

    override fun anchor(): Node<*>? =
        anchor

    companion object {
        fun anchor(anchor: Node<*>, nodeFactory: NodeFactory): RoutingAction =
                AnchoredAttachRoutingAction(
                    anchor = anchor,
                    nodeFactory = nodeFactory
                )
    }
}
