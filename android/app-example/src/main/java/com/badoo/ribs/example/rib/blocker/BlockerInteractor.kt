package com.badoo.ribs.example.rib.blocker

import androidx.lifecycle.Lifecycle
import com.badoo.mvicore.android.lifecycle.startStop
import com.badoo.mvicore.binder.using
import com.badoo.ribs.core.Interactor
import com.badoo.ribs.core.builder.BuildParams
import com.badoo.ribs.example.rib.blocker.mapper.ViewEventToOutput
import io.reactivex.functions.Consumer

class BlockerInteractor(
    buildParams: BuildParams<Nothing?>,
    private val output: Consumer<Blocker.Output>
) : Interactor<BlockerView>(
    buildParams = buildParams,
    disposables = null
) {

    override fun onViewCreated(view: BlockerView, viewLifecycle: Lifecycle) {
        viewLifecycle.startStop {
            bind(view to output using ViewEventToOutput)
        }
    }
}
