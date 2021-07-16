package com.badoo.ribs.sandbox.rib.lorem_ipsum

import androidx.lifecycle.Lifecycle
import com.badoo.mvicore.android.lifecycle.startStop
import com.badoo.binder.using
import com.badoo.ribs.clienthelper.interactor.Interactor
import com.badoo.ribs.core.modality.BuildParams
import com.badoo.ribs.sandbox.rib.lorem_ipsum.mapper.ViewEventToOutput

class LoremIpsumInteractor(
    buildParams: BuildParams<Nothing?>
) : Interactor<LoremIpsum, LoremIpsumView>(
    buildParams = buildParams
) {

    override fun onViewCreated(view: LoremIpsumView, viewLifecycle: Lifecycle) {
        viewLifecycle.startStop {
            bind(view to rib.output using ViewEventToOutput)
        }
    }
}
