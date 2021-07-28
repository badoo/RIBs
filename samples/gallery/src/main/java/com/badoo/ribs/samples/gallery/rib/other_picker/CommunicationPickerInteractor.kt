package com.badoo.ribs.samples.gallery.rib.other_picker

import androidx.lifecycle.Lifecycle
import com.badoo.binder.using
import com.badoo.mvicore.android.lifecycle.startStop
import com.badoo.ribs.clienthelper.interactor.Interactor
import com.badoo.ribs.core.modality.BuildParams
import com.badoo.ribs.samples.gallery.rib.other_picker.mapper.ViewEventToOutput

internal class OtherPickerInteractor(
    buildParams: BuildParams<*>,
) : Interactor<OtherPicker, OtherPickerView>(
    buildParams = buildParams
) {

    override fun onViewCreated(view: OtherPickerView, viewLifecycle: Lifecycle) {
        viewLifecycle.startStop {
            bind(view to rib.output using ViewEventToOutput)
        }
    }
}
