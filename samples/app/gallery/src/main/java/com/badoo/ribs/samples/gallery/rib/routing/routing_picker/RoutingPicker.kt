package com.badoo.ribs.samples.gallery.rib.routing.routing_picker

import com.badoo.ribs.rx2.clienthelper.connector.Connectable
import com.badoo.ribs.core.Rib
import com.badoo.ribs.samples.gallery.rib.routing.routing_picker.RoutingPicker.Input
import com.badoo.ribs.samples.gallery.rib.routing.routing_picker.RoutingPicker.Output
import com.bumble.appyx.utils.customisations.NodeCustomisation

interface RoutingPicker : Rib, Connectable<Input, Output> {

    interface Dependency

    sealed class Input

    sealed class Output {
        object SimpleRoutingSelected : Output()
        object BackStackSelected : Output()
        object ParameterisedRoutingSelected : Output()
        object TransitionsSelected : Output()
    }

    class Customisation(
        val viewFactory: RoutingPickerView.Factory = RoutingPickerViewImpl.Factory()
    ) : NodeCustomisation
}
