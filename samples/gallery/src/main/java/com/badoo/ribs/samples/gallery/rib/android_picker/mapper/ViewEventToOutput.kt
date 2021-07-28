package com.badoo.ribs.samples.gallery.rib.android_picker.mapper

import com.badoo.ribs.samples.gallery.rib.android_picker.AndroidPicker
import com.badoo.ribs.samples.gallery.rib.android_picker.AndroidPicker.Output.*
import com.badoo.ribs.samples.gallery.rib.android_picker.AndroidPickerView.Event

internal object ViewEventToOutput : (Event) -> AndroidPicker.Output? {

    override fun invoke(event: Event): AndroidPicker.Output =
        when (event) {
            Event.LaunchingActivitiesClicked -> ActivitiesExampleSelected
            Event.PermissionsClicked -> PermissionsExampleSelected
            Event.DialogsClicked -> DialogsExampleSelected
        }
}
