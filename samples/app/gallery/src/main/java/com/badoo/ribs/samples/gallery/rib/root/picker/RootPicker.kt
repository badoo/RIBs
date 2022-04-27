package com.badoo.ribs.samples.gallery.rib.root.picker

import com.badoo.ribs.rx2.clienthelper.connector.Connectable
import com.badoo.ribs.core.Rib
import com.badoo.ribs.samples.gallery.rib.root.picker.RootPicker.Input
import com.badoo.ribs.samples.gallery.rib.root.picker.RootPicker.Output
import com.bumble.appyx.utils.customisations.NodeCustomisation

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
    ) : NodeCustomisation
}
