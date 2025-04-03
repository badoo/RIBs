package com.badoo.ribs.samples.communication.menu_example.rib.menu

import android.os.Parcelable
import com.badoo.ribs.clienthelper.connector.Connectable
import com.badoo.ribs.core.Rib
import com.badoo.ribs.samples.communication.menu_example.rib.menu.Menu.Input
import com.badoo.ribs.samples.communication.menu_example.rib.menu.Menu.Output
import kotlinx.parcelize.Parcelize

interface Menu : Rib, Connectable<Input, Output> {

    interface Dependency

    sealed class Input {
        data class SelectMenuItem(val item: MenuItem) : Input()
    }

    sealed class Output {
        data class MenuItemSelected(val item: MenuItem) : Output()
    }

    sealed class MenuItem : Parcelable {
        @Parcelize
        data object Child1 : MenuItem()

        @Parcelize
        data object Child2 : MenuItem()

        @Parcelize
        data object Child3 : MenuItem()
    }
}
