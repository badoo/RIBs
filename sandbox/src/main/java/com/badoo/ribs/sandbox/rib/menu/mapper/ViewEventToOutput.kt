package com.badoo.ribs.sandbox.rib.menu.mapper

import com.badoo.ribs.sandbox.rib.menu.Menu.Output
import com.badoo.ribs.sandbox.rib.menu.MenuView

internal object ViewEventToOutput : (MenuView.Event) -> Output {

    override fun invoke(event: MenuView.Event): Output = when (event) {
        is MenuView.Event.Select -> Output.MenuItemSelected(
            menuItem = event.menuItem
        )
    }

}
