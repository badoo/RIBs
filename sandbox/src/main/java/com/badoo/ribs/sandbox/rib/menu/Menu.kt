package com.badoo.ribs.sandbox.rib.menu

import android.os.Parcelable
import com.badoo.ribs.rx2.clienthelper.connector.Connectable
import com.badoo.ribs.core.Rib
import com.badoo.ribs.core.customisation.RibCustomisation
import com.badoo.ribs.sandbox.rib.menu.Menu.Input
import com.badoo.ribs.sandbox.rib.menu.Menu.Output
import kotlinx.parcelize.Parcelize

interface Menu : Rib, Connectable<Input, Output> {

    interface Dependency

    sealed class Input {
        data class SelectMenuItem(val menuItem: MenuItem) : Input()
    }

    sealed class Output {
        data class MenuItemSelected(val menuItem: MenuItem) : Output()
    }

    sealed class MenuItem : Parcelable {
        @Parcelize object HelloWorld : MenuItem()
        @Parcelize object FooBar : MenuItem()
        @Parcelize object Dialogs : MenuItem()
        @Parcelize object Compose : MenuItem()

    }

    class Customisation(
        val viewFactory: MenuView.Factory = MenuViewImpl.Factory()
    ) : RibCustomisation
}
