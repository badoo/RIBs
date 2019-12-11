package com.badoo.ribs.example.rib.dialog_lorem_ipsum

import android.os.Bundle
import androidx.lifecycle.Lifecycle
import com.badoo.mvicore.android.lifecycle.startStop
import com.badoo.mvicore.binder.using
import com.badoo.ribs.core.Interactor
import com.badoo.ribs.core.Router
import com.badoo.ribs.example.rib.dialog_lorem_ipsum.DialogLoremIpsumRouter.Configuration
import com.badoo.ribs.example.rib.dialog_lorem_ipsum.mapper.ViewEventToOutput
import io.reactivex.functions.Consumer

class DialogLoremIpsumInteractor(
    savedInstanceState: Bundle?,
    router: Router<Configuration, Nothing, Configuration, Nothing, DialogLoremIpsumView>,
    private val output: Consumer<DialogLoremIpsum.Output>
) : Interactor<Configuration, Configuration, Nothing, DialogLoremIpsumView>(
    savedInstanceState = savedInstanceState,
    router = router,
    disposables = null
) {

    override fun onViewCreated(view: DialogLoremIpsumView, viewLifecycle: Lifecycle) {
        viewLifecycle.startStop {
            bind(view to output using ViewEventToOutput)
        }
    }
}
