package com.badoo.ribs.core.routing.action

import com.badoo.ribs.core.view.RibView
import com.badoo.ribs.core.Node
import com.badoo.ribs.core.routing.backstack.NodeDescriptor

class AttachRibRoutingAction< V : RibView>(
    private val builder: () -> Node<*>
) : RoutingAction<V> {

    override fun createRibs(): List<NodeDescriptor> =
        listOf(
            NodeDescriptor(
                node = builder.invoke(),
                viewAttachMode = Node.ViewAttachMode.PARENT
            )
        )

    companion object {
        fun < V : RibView> attach(builder: () -> Node<*>): RoutingAction<V> =
            AttachRibRoutingAction(builder)
    }
}
