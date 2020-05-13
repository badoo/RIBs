package com.badoo.ribs.sandbox.rib.menu.mapper

import com.badoo.ribs.sandbox.rib.menu.MenuView
import com.badoo.ribs.sandbox.rib.menu.feature.MenuFeature

object StateToViewModel: (MenuFeature.State) -> MenuView.ViewModel {
    override fun invoke(state: MenuFeature.State): MenuView.ViewModel =
        MenuView.ViewModel(state.selected)
}
