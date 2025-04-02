package com.badoo.ribs.samples.gallery.rib.routing.routing_picker.mapper

import com.badoo.ribs.samples.gallery.rib.routing.routing_picker.RoutingPicker
import com.badoo.ribs.samples.gallery.rib.routing.routing_picker.RoutingPicker.Output.BackStackSelected
import com.badoo.ribs.samples.gallery.rib.routing.routing_picker.RoutingPicker.Output.ParameterisedRoutingSelected
import com.badoo.ribs.samples.gallery.rib.routing.routing_picker.RoutingPicker.Output.SimpleRoutingSelected
import com.badoo.ribs.samples.gallery.rib.routing.routing_picker.RoutingPicker.Output.TransitionsSelected
import com.badoo.ribs.samples.gallery.rib.routing.routing_picker.RoutingPickerView.Event

internal object ViewEventToOutput : (Event) -> RoutingPicker.Output {

    override fun invoke(event: Event): RoutingPicker.Output =
        when (event) {
            Event.SimpleRoutingClicked -> SimpleRoutingSelected
            Event.BackStackClicked -> BackStackSelected
            Event.ParameterisedRoutingClicked -> ParameterisedRoutingSelected
            Event.TransitionsClicked -> TransitionsSelected
        }
}
