package com.badoo.ribs.samples.comms_nodes_1.rib.menu

import android.view.ViewGroup
import androidx.annotation.LayoutRes
import com.badoo.ribs.core.customisation.inflate
import com.badoo.ribs.core.view.AndroidRibView
import com.badoo.ribs.core.view.RibView
import com.badoo.ribs.core.view.ViewFactory
import com.badoo.ribs.samples.comms_nodes_1.R

interface MenuView : RibView {

    interface Factory : ViewFactory<Nothing?, MenuView>
}


class MenuViewImpl private constructor(
    override val androidView: ViewGroup
) : AndroidRibView(),
    MenuView {

    class Factory(
        @LayoutRes private val layoutRes: Int = R.layout.rib_menu
    ) : MenuView.Factory {
        override fun invoke(deps: Nothing?): (RibView) -> MenuView = {
            MenuViewImpl(
                it.inflate(layoutRes)
            )
        }
    }
}
