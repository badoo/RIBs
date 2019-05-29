package com.badoo.ribs.core.routing.action

import com.badoo.ribs.core.Node
import com.badoo.ribs.core.view.RibView

class AttachRibRoutingAction< V : RibView>(
    private val builder: () -> Node<*>
) : RoutingAction<V> {

    override fun buildNodes(): List<Node.Descriptor> =
        listOf(
            Node.Descriptor(
                node = builder.invoke(),
                viewAttachMode = Node.ViewAttachMode.PARENT
            )
        )

    companion object {
        fun < V : RibView> attach(builder: () -> Node<*>): RoutingAction<V> =
            AttachRibRoutingAction(builder)
    }
}
