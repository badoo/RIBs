package com.badoo.ribs.samples.menu_example.rib.menu_example

import android.view.ViewGroup
import androidx.annotation.LayoutRes
import com.badoo.ribs.core.Node
import com.badoo.ribs.core.customisation.inflate
import com.badoo.ribs.core.view.AndroidRibView
import com.badoo.ribs.core.view.RibView
import com.badoo.ribs.core.view.ViewFactory
import com.badoo.ribs.core.view.ViewFactoryBuilder
import com.badoo.ribs.samples.menu_example.R
import com.badoo.ribs.samples.menu_example.rib.menu.Menu

interface MenuExampleView : RibView {

    interface Factory : ViewFactoryBuilder<Nothing?, MenuExampleView>
}

class MenuExampleViewImpl private constructor(
    override val androidView: ViewGroup
) : AndroidRibView(),
    MenuExampleView {

    class Factory(
        @LayoutRes private val layoutRes: Int = R.layout.rib_container
    ) : MenuExampleView.Factory {
        override fun invoke(deps: Nothing?): ViewFactory<MenuExampleView> = ViewFactory {
            MenuExampleViewImpl(
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
