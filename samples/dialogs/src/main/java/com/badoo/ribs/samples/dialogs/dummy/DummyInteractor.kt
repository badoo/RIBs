package com.badoo.ribs.samples.dialogs.dummy

import androidx.lifecycle.Lifecycle
import com.badoo.mvicore.android.lifecycle.startStop
import com.badoo.mvicore.binder.using
import com.badoo.ribs.clienthelper.interactor.Interactor
import com.badoo.ribs.core.modality.BuildParams

class DummyInteractor(
    buildParams: BuildParams<Nothing?>
) : Interactor<Dummy, DummyView>(
    buildParams = buildParams
) {

    override fun onViewCreated(view: DummyView, viewLifecycle: Lifecycle) {
        viewLifecycle.startStop {
            bind(view to rib.output using ViewEventToOutput)
        }
    }
}
