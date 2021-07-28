package com.badoo.ribs.samples.gallery.rib.other_picker.mapper

import com.badoo.ribs.samples.gallery.rib.other_picker.OtherPicker
import com.badoo.ribs.samples.gallery.rib.other_picker.OtherPicker.Output.RetainedInstanceStoreSelected
import com.badoo.ribs.samples.gallery.rib.other_picker.OtherPickerView.Event

internal object ViewEventToOutput : (Event) -> OtherPicker.Output? {

    override fun invoke(event: Event): OtherPicker.Output =
        when (event) {
            Event.RetainedInstanceStoreClicked -> RetainedInstanceStoreSelected
        }
}
