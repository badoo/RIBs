package com.badoo.ribs.example.rib.lorem_ipsum

import com.badoo.ribs.core.builder.BuildParams
import androidx.lifecycle.Lifecycle
import com.badoo.mvicore.android.lifecycle.startStop
import com.badoo.mvicore.binder.using
import com.badoo.ribs.core.Interactor
import com.badoo.ribs.example.rib.lorem_ipsum.mapper.ViewEventToOutput
import io.reactivex.functions.Consumer

class LoremIpsumInteractor(
    buildParams: BuildParams<Nothing?>,
    private val output: Consumer<LoremIpsum.Output>
) : Interactor<LoremIpsumView>(
    buildParams = buildParams,
    disposables = null
) {

    override fun onViewCreated(view: LoremIpsumView, viewLifecycle: Lifecycle) {
        viewLifecycle.startStop {
            bind(view to output using ViewEventToOutput)
        }
    }
}
