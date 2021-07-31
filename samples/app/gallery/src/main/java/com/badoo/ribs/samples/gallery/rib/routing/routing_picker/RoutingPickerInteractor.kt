package com.badoo.ribs.samples.gallery.rib.routing.routing_picker

import androidx.lifecycle.Lifecycle
import com.badoo.binder.using
import com.badoo.mvicore.android.lifecycle.startStop
import com.badoo.ribs.clienthelper.interactor.Interactor
import com.badoo.ribs.core.modality.BuildParams
import com.badoo.ribs.samples.gallery.rib.routing.routing_picker.mapper.ViewEventToOutput

internal class RoutingPickerInteractor(
    buildParams: BuildParams<*>,
) : Interactor<RoutingPicker, RoutingPickerView>(
    buildParams = buildParams
) {

    override fun onViewCreated(view: RoutingPickerView, viewLifecycle: Lifecycle) {
        viewLifecycle.startStop {
            bind(view to rib.output using ViewEventToOutput)
        }
    }
}
