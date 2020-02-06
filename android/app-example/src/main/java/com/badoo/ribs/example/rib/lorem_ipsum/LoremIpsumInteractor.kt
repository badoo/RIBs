package com.badoo.ribs.example.rib.lorem_ipsum

import com.badoo.ribs.core.BuildParams
import androidx.lifecycle.Lifecycle
import com.badoo.mvicore.android.lifecycle.startStop
import com.badoo.mvicore.binder.using
import com.badoo.ribs.core.Interactor
import com.badoo.ribs.core.Router
import com.badoo.ribs.example.rib.lorem_ipsum.LoremIpsumRouter.Configuration
import com.badoo.ribs.example.rib.lorem_ipsum.mapper.ViewEventToOutput
import io.reactivex.functions.Consumer

class LoremIpsumInteractor(
    buildParams: BuildParams<Nothing?>,
    router: Router<Configuration, Nothing, Configuration, Nothing, LoremIpsumView>,
    private val output: Consumer<LoremIpsum.Output>
) : Interactor<Configuration, Configuration, Nothing, LoremIpsumView>(
    buildParams = buildParams,
    router = router,
    disposables = null
) {

    override fun onViewCreated(view: LoremIpsumView, viewLifecycle: Lifecycle) {
        viewLifecycle.startStop {
            bind(view to output using ViewEventToOutput)
        }
    }
}
