package com.badoo.ribs.samples.gallery.rib.android.picker.mapper

import com.badoo.ribs.samples.gallery.rib.android.picker.AndroidPicker
import com.badoo.ribs.samples.gallery.rib.android.picker.AndroidPicker.Output.*
import com.badoo.ribs.samples.gallery.rib.android.picker.AndroidPickerView.Event

internal object ViewEventToOutput : (Event) -> AndroidPicker.Output? {

    override fun invoke(event: Event): AndroidPicker.Output =
        when (event) {
            Event.LaunchingActivitiesClicked -> ActivitiesExampleSelected
            Event.PermissionsClicked -> PermissionsExampleSelected
            Event.DialogsClicked -> DialogsExampleSelected
        }
}
