package com.badoo.ribs.example.rib.blocker

import com.badoo.ribs.core.BuildParams
import androidx.lifecycle.Lifecycle
import com.badoo.mvicore.android.lifecycle.startStop
import com.badoo.mvicore.binder.using
import com.badoo.ribs.core.Interactor
import com.badoo.ribs.core.Router
import com.badoo.ribs.example.rib.blocker.BlockerRouter.Configuration
import com.badoo.ribs.example.rib.blocker.mapper.ViewEventToOutput
import io.reactivex.functions.Consumer

class BlockerInteractor(
    buildParams: BuildParams<Nothing?>,
    router: Router<Configuration, Nothing, Configuration, Nothing, BlockerView>,
    private val output: Consumer<Blocker.Output>
) : Interactor<Configuration, Configuration, Nothing, BlockerView>(
    buildParams = buildParams,
    router = router,
    disposables = null
) {

    override fun onViewCreated(view: BlockerView, viewLifecycle: Lifecycle) {
        viewLifecycle.startStop {
            bind(view to output using ViewEventToOutput)
        }
    }
}
