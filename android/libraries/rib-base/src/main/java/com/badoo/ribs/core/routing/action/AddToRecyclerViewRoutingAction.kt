package com.badoo.ribs.core.routing.action

import android.os.Bundle
import com.badoo.ribs.core.Node
import com.badoo.ribs.core.view.RibView

open class AddToRecyclerViewRoutingAction<V : RibView>(
    private val builder: (savedInstanceState: Bundle?) -> Node<*>
) : AttachRibRoutingAction<V>(builder) {

    override fun buildNodes(bundles: List<Bundle?>): List<Node.Descriptor> =
        super.buildNodes(bundles).map {
            it.copy(
                node = builder.invoke(bundles.firstOrNull()),
                viewAttachMode = Node.AttachMode.DEFERRED
            )
        }

    companion object {
        fun <V : RibView> recyclerView(builder: (savedInstanceState: Bundle?) -> Node<*>): RoutingAction<V> =
            AddToRecyclerViewRoutingAction(builder)
    }
}
