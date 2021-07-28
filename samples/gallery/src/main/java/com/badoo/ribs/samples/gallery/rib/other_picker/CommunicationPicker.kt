package com.badoo.ribs.samples.gallery.rib.other_picker

import com.badoo.ribs.rx2.clienthelper.connector.Connectable
import com.badoo.ribs.core.Rib
import com.badoo.ribs.core.customisation.RibCustomisation
import com.badoo.ribs.samples.gallery.rib.other_picker.OtherPicker.Input
import com.badoo.ribs.samples.gallery.rib.other_picker.OtherPicker.Output
import io.reactivex.Single

interface OtherPicker : Rib, Connectable<Input, Output> {

    interface Dependency

    sealed class Input

    sealed class Output {
        object RetainedInstanceStoreSelected : Output()
    }

    class Customisation(
        val viewFactory: OtherPickerView.Factory = OtherPickerViewImpl.Factory()
    ) : RibCustomisation
}
