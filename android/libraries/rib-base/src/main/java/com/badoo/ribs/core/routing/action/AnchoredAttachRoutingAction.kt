package com.badoo.ribs.core.routing.action

import com.badoo.ribs.core.BuildContext
import com.badoo.ribs.core.Node
import com.badoo.ribs.core.view.RibView

class AnchoredAttachRoutingAction<V : RibView>(
    private val anchor: Node<*>,
    builder: (buildContext: BuildContext) -> Node<*>
) : AttachRibRoutingAction<V>(
    builder = builder
) {

    override fun anchor(): Node<*>? =
        anchor

    companion object {
        fun <V : RibView> anchor(
            anchor: Node<*>,
            builder: (buildContext: BuildContext) -> Node<*>): RoutingAction<V> =
                AnchoredAttachRoutingAction(
                    anchor,
                    builder
                )
    }
}
