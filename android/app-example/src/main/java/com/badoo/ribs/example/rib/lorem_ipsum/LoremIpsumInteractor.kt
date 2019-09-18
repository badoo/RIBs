package com.badoo.ribs.example.rib.lorem_ipsum

import androidx.lifecycle.Lifecycle
import com.badoo.mvicore.android.lifecycle.createDestroy
import com.badoo.mvicore.binder.using
import com.badoo.ribs.core.Interactor
import com.badoo.ribs.core.Router
import com.badoo.ribs.example.rib.lorem_ipsum.LoremIpsumRouter.Configuration
import com.badoo.ribs.example.rib.lorem_ipsum.mapper.ViewEventToOutput
import io.reactivex.functions.Consumer
import android.os.Bundle

class LoremIpsumInteractor(
    savedInstanceState: Bundle?,
    router: Router<Configuration, Nothing, Configuration, Nothing, LoremIpsumView>,
    private val output: Consumer<LoremIpsum.Output>
) : Interactor<Configuration, Configuration, Nothing, LoremIpsumView>(
    savedInstanceState = savedInstanceState,
    router = router,
    disposables = null
) {

    override fun onViewCreated(view: LoremIpsumView, viewLifecycle: Lifecycle) {
        viewLifecycle.createDestroy {
            bind(view to output using ViewEventToOutput)
        }
    }
}
