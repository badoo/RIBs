package com.badoo.ribs.samples.gallery.rib.root.picker

import com.badoo.ribs.core.Rib
import com.badoo.ribs.core.customisation.RibCustomisation
import com.badoo.ribs.rx3.clienthelper.connector.Connectable
import com.badoo.ribs.samples.gallery.rib.root.picker.RootPicker.Input
import com.badoo.ribs.samples.gallery.rib.root.picker.RootPicker.Output

interface RootPicker : Rib, Connectable<Input, Output> {

    interface Dependency

    sealed class Input

    sealed class Output {
        data object AndroidSelected : Output()
        data object CommunicationSelected : Output()
        data object RoutingSelected : Output()
        data object OtherSelected : Output()
    }

    class Customisation(
        val viewFactory: RootPickerView.Factory = RootPickerViewImpl.Factory()
    ) : RibCustomisation
}
