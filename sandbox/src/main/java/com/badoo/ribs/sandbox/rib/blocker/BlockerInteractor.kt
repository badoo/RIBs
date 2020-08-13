package com.badoo.ribs.sandbox.rib.blocker

import androidx.lifecycle.Lifecycle
import com.badoo.mvicore.android.lifecycle.startStop
import com.badoo.mvicore.binder.using
import com.badoo.ribs.clienthelper.interactor.Interactor
import com.badoo.ribs.core.modality.BuildParams
import com.badoo.ribs.sandbox.rib.blocker.mapper.ViewEventToOutput

class BlockerInteractor(
    buildParams: BuildParams<Nothing?>
) : Interactor<Blocker, BlockerView>(
    buildParams = buildParams
) {

    override fun onViewCreated(view: BlockerView, viewLifecycle: Lifecycle) {
        viewLifecycle.startStop {
            bind(view to rib.output using ViewEventToOutput)
        }
    }
}
