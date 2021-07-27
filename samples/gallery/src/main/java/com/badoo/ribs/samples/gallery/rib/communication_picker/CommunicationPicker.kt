package com.badoo.ribs.samples.gallery.rib.communication_picker

import com.badoo.ribs.rx2.clienthelper.connector.Connectable
import com.badoo.ribs.core.Rib
import com.badoo.ribs.core.customisation.RibCustomisation
import com.badoo.ribs.samples.gallery.rib.communication_picker.CommunicationPicker.Input
import com.badoo.ribs.samples.gallery.rib.communication_picker.CommunicationPicker.Output
import io.reactivex.Single

interface CommunicationPicker : Rib, Connectable<Input, Output> {

    interface Dependency

    sealed class Input

    sealed class Output {
        object MenuExampleSelected : Output()
        object MultiScreenExampleSelected : Output()
    }

    class Customisation(
        val viewFactory: CommunicationPickerView.Factory = CommunicationPickerViewImpl.Factory()
    ) : RibCustomisation
}
