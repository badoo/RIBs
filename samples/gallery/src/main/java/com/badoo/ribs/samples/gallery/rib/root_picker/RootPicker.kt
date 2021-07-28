package com.badoo.ribs.samples.gallery.rib.root_picker

import com.badoo.ribs.rx2.clienthelper.connector.Connectable
import com.badoo.ribs.core.Rib
import com.badoo.ribs.core.customisation.RibCustomisation
import com.badoo.ribs.samples.gallery.rib.root_picker.RootPicker.Input
import com.badoo.ribs.samples.gallery.rib.root_picker.RootPicker.Output

interface RootPicker : Rib, Connectable<Input, Output> {

    interface Dependency

    sealed class Input

    sealed class Output {
        object AndroidSelected : Output()
        object CommunicationSelected : Output()
        object RoutingSelected : Output()
        object OtherSelected : Output()
    }

    class Customisation(
        val viewFactory: RootPickerView.Factory = RootPickerViewImpl.Factory()
    ) : RibCustomisation
}
