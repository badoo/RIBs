package com.badoo.ribs.routing.action

import com.badoo.ribs.core.Node
import com.badoo.ribs.core.builder.RibFactory

class AnchoredAttachRoutingAction(
    private val anchor: Node<*>,
    ribFactory: RibFactory
) : AttachRibRoutingAction(
    ribFactory = ribFactory
) {

    override fun anchor(): Node<*>? =
        anchor

    companion object {
        fun anchor(anchor: Node<*>, ribFactory: RibFactory): RoutingAction =
                AnchoredAttachRoutingAction(
                    anchor = anchor,
                    ribFactory = ribFactory
                )
    }
}
