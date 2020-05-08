package com.badoo.ribs.sandbox.rib.menu.mapper

import com.badoo.ribs.sandbox.rib.menu.Menu
import com.badoo.ribs.sandbox.rib.menu.MenuView

internal object ViewEventToOutput : (MenuView.Event) -> Menu.Output? {

    override fun invoke(event: MenuView.Event): Menu.Output? = when (event) {
        is MenuView.Event.Select -> Menu.Output.MenuItemSelected(
            menuItem = event.menuItem
        )
    }

}
