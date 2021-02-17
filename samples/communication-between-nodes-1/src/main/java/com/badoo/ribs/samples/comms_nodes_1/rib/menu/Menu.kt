package com.badoo.ribs.samples.comms_nodes_1.rib.menu

import android.os.Parcelable
import com.badoo.ribs.clienthelper.connector.Connectable
import com.badoo.ribs.core.Rib
import com.badoo.ribs.samples.comms_nodes_1.rib.menu.Menu.Input
import com.badoo.ribs.samples.comms_nodes_1.rib.menu.Menu.Output
import kotlinx.android.parcel.Parcelize

interface Menu : Rib, Connectable<Input, Output> {

    interface Dependency

    sealed class Input {
        data class SelectMenuItem(val item: MenuItem) : Input()
    }

    sealed class Output {
        data class MenuItemSelected(val item: MenuItem) : Output()
    }

    sealed class MenuItem : Parcelable {
        @Parcelize object Child1 : MenuItem()
        @Parcelize object Child2 : MenuItem()
        @Parcelize object Child3 : MenuItem()
    }
}
