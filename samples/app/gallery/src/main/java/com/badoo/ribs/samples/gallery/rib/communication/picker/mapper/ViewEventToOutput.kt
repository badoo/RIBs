package com.badoo.ribs.samples.gallery.rib.communication.picker.mapper

import com.badoo.ribs.samples.gallery.rib.communication.picker.CommunicationPicker
import com.badoo.ribs.samples.gallery.rib.communication.picker.CommunicationPicker.Output.MenuExampleSelected
import com.badoo.ribs.samples.gallery.rib.communication.picker.CommunicationPicker.Output.CoordinateMultipleSelected
import com.badoo.ribs.samples.gallery.rib.communication.picker.CommunicationPickerView.Event

internal object ViewEventToOutput : (Event) -> CommunicationPicker.Output? {

    override fun invoke(event: Event): CommunicationPicker.Output =
        when (event) {
            Event.MenuExampleClicked -> MenuExampleSelected
            Event.CoordinateMultipleExampleClicked -> CoordinateMultipleSelected
        }
}
