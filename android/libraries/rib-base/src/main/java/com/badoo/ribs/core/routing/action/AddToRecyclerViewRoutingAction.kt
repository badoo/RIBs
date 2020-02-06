package com.badoo.ribs.core.routing.action

import android.os.Bundle
import com.badoo.ribs.core.AttachMode
import com.badoo.ribs.core.Node
import com.badoo.ribs.core.builder.BuildContext
import com.badoo.ribs.core.routing.portal.AncestryInfo
import com.badoo.ribs.core.view.RibView

open class AddToRecyclerViewRoutingAction<V : RibView>(
    private val builder: (buildContext: BuildContext) -> Node<*>
) : RoutingAction<V> {

    override fun buildNodes(ancestryInfo: AncestryInfo, bundles: List<Bundle?>): List<Node<*>> =
        listOf(
            builder.invoke(
                BuildContext(
                    ancestryInfo = ancestryInfo,
                    viewAttachMode = AttachMode.EXTERNAL,
                    savedInstanceState = bundles.firstOrNull()
                )
            )
        )

    companion object {
        fun <V : RibView> recyclerView(builder: (buildContext: BuildContext) -> Node<*>): RoutingAction<V> =
            AddToRecyclerViewRoutingAction(builder)
    }
}
