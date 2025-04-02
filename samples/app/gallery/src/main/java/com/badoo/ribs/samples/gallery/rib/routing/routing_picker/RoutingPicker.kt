package com.badoo.ribs.samples.gallery.rib.routing.routing_picker

import com.badoo.ribs.rx3.clienthelper.connector.Connectable
import com.badoo.ribs.core.Rib
import com.badoo.ribs.core.customisation.RibCustomisation
import com.badoo.ribs.samples.gallery.rib.routing.routing_picker.RoutingPicker.Input
import com.badoo.ribs.samples.gallery.rib.routing.routing_picker.RoutingPicker.Output

interface RoutingPicker : Rib, Connectable<Input, Output> {

    interface Dependency

    sealed class Input

    sealed class Output {
        data object SimpleRoutingSelected : Output()
        data object BackStackSelected : Output()
        data object ParameterisedRoutingSelected : Output()
        data object TransitionsSelected : Output()
    }

    class Customisation(
        val viewFactory: RoutingPickerView.Factory = RoutingPickerViewImpl.Factory()
    ) : RibCustomisation
}
