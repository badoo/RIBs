package com.badoo.ribs.example.rib.lorem_ipsum

import com.badoo.ribs.core.BuildContext
import androidx.lifecycle.Lifecycle
import com.badoo.mvicore.android.lifecycle.startStop
import com.badoo.mvicore.binder.using
import com.badoo.ribs.core.Interactor
import com.badoo.ribs.core.Router
import com.badoo.ribs.example.rib.lorem_ipsum.LoremIpsumRouter.Configuration
import com.badoo.ribs.example.rib.lorem_ipsum.mapper.ViewEventToOutput
import io.reactivex.functions.Consumer

class LoremIpsumInteractor(
    buildContext: BuildContext.Resolved<Nothing?>,
    router: Router<Configuration, Nothing, Configuration, Nothing, LoremIpsumView>,
    private val output: Consumer<LoremIpsum.Output>
) : Interactor<Configuration, Configuration, Nothing, LoremIpsumView>(
    buildContext = buildContext,
    router = router,
    disposables = null
) {

    override fun onViewCreated(view: LoremIpsumView, viewLifecycle: Lifecycle) {
        viewLifecycle.startStop {
            bind(view to output using ViewEventToOutput)
        }
    }
}
