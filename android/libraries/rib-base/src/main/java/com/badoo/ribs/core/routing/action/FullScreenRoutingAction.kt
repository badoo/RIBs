package com.badoo.ribs.core.routing.action

import android.os.Bundle
import com.badoo.ribs.core.Node
import com.badoo.ribs.core.Portal
import com.badoo.ribs.core.Portal.Sink.Command.Add
import com.badoo.ribs.core.Portal.Sink.Command.Remove
import com.badoo.ribs.core.view.RibView

class FullScreenRoutingAction<V : RibView>(
    private val portal: Portal.Sink,
    private val builder: (Bundle?) -> Node<*>
) : RoutingAction<V> {

    override fun buildNodes(bundles: List<Bundle?>): List<Node.Descriptor> =
        listOf(
            Node.Descriptor(
                node = builder.invoke(bundles.firstOrNull()),
                viewAttachMode = Node.AttachMode.EXTERNAL
            )
        )

    override fun execute(nodes: List<Node.Descriptor>) {
        super.execute(nodes)
//        portal.accept(Add(nodes.first().node))
    }

    override fun cleanup(nodes: List<Node.Descriptor>) {
        super.cleanup(nodes)
//        portal.accept(Remove(nodes.first().node))
    }

    companion object {
        fun <V : RibView> fullScreen(portal: Portal.Sink, builder: (Bundle?) -> Node<*>): RoutingAction<V> =
            FullScreenRoutingAction(portal, builder)
    }
}
