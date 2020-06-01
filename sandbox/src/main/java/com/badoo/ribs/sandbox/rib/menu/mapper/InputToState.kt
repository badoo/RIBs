package com.badoo.ribs.sandbox.rib.menu.mapper

import com.badoo.ribs.sandbox.rib.menu.Menu.Input
import com.badoo.ribs.sandbox.rib.menu.feature.MenuFeature

object InputToState: (Input) -> MenuFeature.State {
    override fun invoke(input: Input): MenuFeature.State =
        when (input) {
            is Input.SelectMenuItem -> MenuFeature.State(input.menuItem)
        }
}
