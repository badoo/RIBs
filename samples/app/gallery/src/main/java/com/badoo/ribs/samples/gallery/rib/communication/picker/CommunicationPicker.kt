package com.badoo.ribs.samples.gallery.rib.communication.picker

import com.badoo.ribs.core.Rib
import com.badoo.ribs.core.customisation.RibCustomisation
import com.badoo.ribs.rx3.clienthelper.connector.Connectable
import com.badoo.ribs.samples.gallery.rib.communication.picker.CommunicationPicker.Input
import com.badoo.ribs.samples.gallery.rib.communication.picker.CommunicationPicker.Output

interface CommunicationPicker : Rib, Connectable<Input, Output> {

    interface Dependency

    sealed class Input

    sealed class Output {
        data object MenuExampleSelected : Output()
        data object CoordinateMultipleSelected : Output()
    }

    class Customisation(
        val viewFactory: CommunicationPickerView.Factory = CommunicationPickerViewImpl.Factory()
    ) : RibCustomisation
}
