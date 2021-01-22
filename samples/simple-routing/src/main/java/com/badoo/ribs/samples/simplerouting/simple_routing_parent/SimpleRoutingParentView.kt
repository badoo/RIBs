package com.badoo.ribs.samples.simplerouting.simple_routing_parent

import android.view.ViewGroup
import androidx.annotation.LayoutRes
import com.badoo.ribs.core.Node
import com.badoo.ribs.core.customisation.inflate
import com.badoo.ribs.core.view.AndroidRibView
import com.badoo.ribs.core.view.RibView
import com.badoo.ribs.core.view.ViewFactory
import com.badoo.ribs.samples.simplerouting.R
import com.badoo.ribs.samples.simplerouting.simple_routing_child1.SimpleRoutingChild1
import com.badoo.ribs.samples.simplerouting.simple_routing_child2.SimpleRoutingChild2

interface SimpleRoutingParentView : RibView {

    interface Factory : ViewFactory<Nothing?, SimpleRoutingParentView>
}

class SimpleRoutingParentViewImpl private constructor(
    override val androidView: ViewGroup
) : AndroidRibView(), SimpleRoutingParentView {

    class Factory(
        @LayoutRes private val layoutRes: Int = R.layout.rib_simple_routing_parent
    ) : SimpleRoutingParentView.Factory {
        override fun invoke(deps: Nothing?): (RibView) -> SimpleRoutingParentView = {
            SimpleRoutingParentViewImpl(
                androidView = it.inflate(layoutRes)
            )
        }
    }

    private val child1Container: ViewGroup = androidView.findViewById(R.id.child1_container)
    private val child2Container: ViewGroup = androidView.findViewById(R.id.child2_container)

    override fun getParentViewForSubtree(subtreeOf: Node<*>): ViewGroup =
        when (subtreeOf) {
            is SimpleRoutingChild1 -> child1Container
            is SimpleRoutingChild2 -> child2Container
            else -> super.getParentViewForSubtree(subtreeOf)
        }
}
