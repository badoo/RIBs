package com.badoo.ribs.sandbox.rib.menu.mapper

import com.badoo.ribs.sandbox.rib.menu.Menu
import com.badoo.ribs.sandbox.rib.menu.feature.MenuFeature

object InputToState: (Menu.Input) -> MenuFeature.State {
    override fun invoke(input: Menu.Input): MenuFeature.State =
        when (input) {
            is Menu.Input.SelectMenuItem -> MenuFeature.State(input.menuItem)
        }
}
