package com.badoo.ribs.samples.gallery.rib.root.picker.mapper

import com.badoo.ribs.samples.gallery.rib.root.picker.RootPicker
import com.badoo.ribs.samples.gallery.rib.root.picker.RootPicker.Output.AndroidSelected
import com.badoo.ribs.samples.gallery.rib.root.picker.RootPicker.Output.CommunicationSelected
import com.badoo.ribs.samples.gallery.rib.root.picker.RootPicker.Output.OtherSelected
import com.badoo.ribs.samples.gallery.rib.root.picker.RootPicker.Output.RoutingSelected
import com.badoo.ribs.samples.gallery.rib.root.picker.RootPickerView.Event

internal object ViewEventToOutput : (Event) -> RootPicker.Output {

    override fun invoke(event: Event): RootPicker.Output =
        when (event) {
            Event.AndroidClicked -> AndroidSelected
            Event.CommunicationClicked -> CommunicationSelected
            Event.RoutingClicked -> RoutingSelected
            Event.OtherClicked -> OtherSelected
        }
}
