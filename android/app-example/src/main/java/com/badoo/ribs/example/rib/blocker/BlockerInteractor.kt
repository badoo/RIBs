package com.badoo.ribs.example.rib.blocker

import androidx.lifecycle.Lifecycle
import com.badoo.mvicore.android.lifecycle.createDestroy
import com.badoo.mvicore.binder.using
import com.badoo.ribs.core.Interactor
import com.badoo.ribs.core.Router
import com.badoo.ribs.example.rib.blocker.BlockerRouter.Configuration
import com.badoo.ribs.example.rib.blocker.mapper.ViewEventToOutput
import io.reactivex.functions.Consumer
import android.os.Bundle

class BlockerInteractor(
    savedInstanceState: Bundle?,
    router: Router<Configuration, Nothing, Configuration, Nothing, BlockerView>,
    private val output: Consumer<Blocker.Output>
) : Interactor<Configuration, Configuration, Nothing, BlockerView>(
    savedInstanceState = savedInstanceState,
    router = router,
    disposables = null
) {

    override fun onViewCreated(view: BlockerView, viewLifecycle: Lifecycle) {
        viewLifecycle.createDestroy {
            bind(view to output using ViewEventToOutput)
        }
    }
}
