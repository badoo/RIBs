package com.badoo.ribs.samples.gallery.rib.communication_picker.mapper

import com.badoo.ribs.samples.gallery.rib.communication_picker.CommunicationPicker
import com.badoo.ribs.samples.gallery.rib.communication_picker.CommunicationPicker.Output.MenuExampleSelected
import com.badoo.ribs.samples.gallery.rib.communication_picker.CommunicationPicker.Output.MultiScreenExampleSelected
import com.badoo.ribs.samples.gallery.rib.communication_picker.CommunicationPickerView.Event

internal object ViewEventToOutput : (Event) -> CommunicationPicker.Output? {

    override fun invoke(event: Event): CommunicationPicker.Output =
        when (event) {
            Event.MenuExampleClicked -> MenuExampleSelected
            Event.MultiScreenExampleClicked -> MultiScreenExampleSelected
        }
}
