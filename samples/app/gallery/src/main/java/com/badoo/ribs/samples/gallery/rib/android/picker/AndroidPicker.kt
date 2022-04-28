package com.badoo.ribs.samples.gallery.rib.android.picker

import com.badoo.ribs.rx2.clienthelper.connector.Connectable
import com.badoo.ribs.core.Rib
import com.badoo.ribs.core.customisation.RibCustomisation
import com.badoo.ribs.samples.gallery.rib.android.picker.AndroidPicker.Input
import com.badoo.ribs.samples.gallery.rib.android.picker.AndroidPicker.Output

interface AndroidPicker : Rib, Connectable<Input, Output> {

    interface Dependency

    sealed class Input

    sealed class Output {
        object ActivitiesExampleSelected : Output()
        object PermissionsExampleSelected : Output()
        object DialogsExampleSelected : Output()
    }

    class Customisation(
        val viewFactory: AndroidPickerView.Factory = AndroidPickerViewImpl.Factory()
    ) : RibCustomisation
}
