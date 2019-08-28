package com.badoo.ribs.core.routing.action

import android.os.Bundle
import com.badoo.ribs.core.Node
import com.badoo.ribs.core.view.RibView

class AnchoredAttachRoutingAction<V : RibView>(
    private val virtualParent: Node<*>,
    builder: (Bundle?) -> Node<*>
) : AttachRibRoutingAction<V>(
    builder = builder
) {

    override fun virtualParentNode(): Node<*>? =
        virtualParent

    companion object {
        fun <V : RibView> anchor(
            virtualParent: Node<*>,
            builder: (Bundle?) -> Node<*>): RoutingAction<V> =
            AnchoredAttachRoutingAction(
                virtualParent,
                builder
            )
    }
}
