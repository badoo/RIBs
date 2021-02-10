package com.badoo.ribs.samples.comms_nodes_1.rib.container

import android.view.ViewGroup
import androidx.annotation.LayoutRes
import com.badoo.ribs.core.Node
import com.badoo.ribs.core.customisation.inflate
import com.badoo.ribs.core.view.AndroidRibView
import com.badoo.ribs.core.view.RibView
import com.badoo.ribs.core.view.ViewFactory
import com.badoo.ribs.samples.comms_nodes_1.R
import com.badoo.ribs.samples.comms_nodes_1.rib.child1.Child1
import com.badoo.ribs.samples.comms_nodes_1.rib.child2.Child2
import com.badoo.ribs.samples.comms_nodes_1.rib.child3.Child3
import com.badoo.ribs.samples.comms_nodes_1.rib.menu.Menu

interface ContainerView : RibView {

    interface Factory : ViewFactory<Nothing?, ContainerView>
}


class ContainerViewImpl private constructor(
    override val androidView: ViewGroup
) : AndroidRibView(),
    ContainerView {

    class Factory(
        @LayoutRes private val layoutRes: Int = R.layout.rib_container
    ) : ContainerView.Factory {
        override fun invoke(deps: Nothing?): (RibView) -> ContainerView = {
            ContainerViewImpl(
                it.inflate(layoutRes)
            )
        }
    }

    private val menuContainer: ViewGroup = androidView.findViewById(R.id.menu_container)
    private val childContainer: ViewGroup = androidView.findViewById(R.id.child_container)

    override fun getParentViewForSubtree(subtreeOf: Node<*>): ViewGroup =
        when (subtreeOf) {
            is Menu -> menuContainer
            is Child1, is Child2, is Child3 -> childContainer
            else -> super.getParentViewForSubtree(subtreeOf)
        }
}
