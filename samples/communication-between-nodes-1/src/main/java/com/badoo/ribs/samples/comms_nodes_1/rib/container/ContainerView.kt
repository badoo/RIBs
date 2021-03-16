package com.badoo.ribs.samples.comms_nodes_1.rib.container

import android.view.ViewGroup
import androidx.annotation.LayoutRes
import com.badoo.ribs.core.Node
import com.badoo.ribs.core.customisation.inflate
import com.badoo.ribs.core.view.AndroidRibView
import com.badoo.ribs.core.view.RibView
import com.badoo.ribs.core.view.ViewFactory
import com.badoo.ribs.core.view.ViewFactoryBuilder
import com.badoo.ribs.samples.comms_nodes_1.R
import com.badoo.ribs.samples.comms_nodes_1.rib.menu.Menu

interface ContainerView : RibView {

    interface Factory : ViewFactoryBuilder<Nothing?, ContainerView>
}

class ContainerViewImpl private constructor(
    override val androidView: ViewGroup
) : AndroidRibView(),
    ContainerView {

    class Factory(
        @LayoutRes private val layoutRes: Int = R.layout.rib_container
    ) : ContainerView.Factory {
        override fun invoke(deps: Nothing?): ViewFactory<ContainerView> = ViewFactory {
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
            else -> childContainer
        }
}
