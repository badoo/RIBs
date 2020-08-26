package com.badoo.ribs.routing.action

import com.badoo.ribs.core.Node

class AnchoredAttachResolution(
    private val anchor: Node<*>,
    ribFactory: RibFactory
) : AttachRibResolution(
    ribFactory = ribFactory
) {

    override fun anchor(): Node<*>? =
        anchor

    companion object {
        fun anchor(anchor: Node<*>, ribFactory: RibFactory): Resolution =
                AnchoredAttachResolution(
                    anchor = anchor,
                    ribFactory = ribFactory
                )
    }
}
