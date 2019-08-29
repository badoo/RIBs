package com.badoo.ribs.core.routing.action

import android.os.Bundle
import com.badoo.ribs.core.Node
import com.badoo.ribs.core.view.RibView

class AnchoredAttachRoutingAction<V : RibView>(
    private val anchor: Node<*>,
    builder: (Bundle?) -> Node<*>
) : AttachRibRoutingAction<V>(
    builder = builder
) {

    override fun anchor(): Node<*>? =
        anchor

    companion object {
        fun <V : RibView> anchor(
            anchor: Node<*>,
            builder: (Bundle?) -> Node<*>): RoutingAction<V> =
            AnchoredAttachRoutingAction(
                anchor,
                builder
            )
    }
}
