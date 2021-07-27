package com.badoo.ribs.samples.gallery.rib.communication_picker

import androidx.lifecycle.Lifecycle
import com.badoo.binder.using
import com.badoo.mvicore.android.lifecycle.startStop
import com.badoo.ribs.clienthelper.interactor.Interactor
import com.badoo.ribs.core.modality.BuildParams
import com.badoo.ribs.samples.gallery.rib.communication_picker.mapper.ViewEventToOutput

internal class CommunicationPickerInteractor(
    buildParams: BuildParams<*>,
) : Interactor<CommunicationPicker, CommunicationPickerView>(
    buildParams = buildParams
) {

    override fun onViewCreated(view: CommunicationPickerView, viewLifecycle: Lifecycle) {
        viewLifecycle.startStop {
            bind(view to rib.output using ViewEventToOutput)
        }
    }
}
