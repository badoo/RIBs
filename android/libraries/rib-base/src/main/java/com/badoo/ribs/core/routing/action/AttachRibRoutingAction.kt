package com.badoo.ribs.core.routing.action

import android.os.Bundle
import com.badoo.ribs.core.AttachMode
import com.badoo.ribs.core.Node
import com.badoo.ribs.core.BuildParams
import com.badoo.ribs.core.routing.portal.AncestryInfo
import com.badoo.ribs.core.view.RibView

open class AttachRibRoutingAction<V : RibView>(
    private val builder: (buildContext: BuildParams.BuildContext) -> Node<*>
) : RoutingAction<V> {

    override fun buildNodes(ancestryInfo: AncestryInfo, bundles: List<Bundle?>): List<Node<*>> =
        listOf(
            builder.invoke(
                BuildParams.BuildContext(
                    ancestryInfo = ancestryInfo,
                    viewAttachMode = AttachMode.PARENT,
                    savedInstanceState = bundles.firstOrNull()
                )
            )
        )

    companion object {
        fun <V : RibView> attach(builder: (buildContext: BuildParams.BuildContext) -> Node<*>): RoutingAction<V> =
            AttachRibRoutingAction(builder)
    }
}
