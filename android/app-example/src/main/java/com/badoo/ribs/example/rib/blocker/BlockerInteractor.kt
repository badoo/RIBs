package com.badoo.ribs.example.rib.blocker

import android.os.Bundle
import androidx.lifecycle.Lifecycle
import com.badoo.mvicore.android.lifecycle.startStop
import com.badoo.mvicore.binder.using
import com.badoo.ribs.core.Interactor
import com.badoo.ribs.example.rib.blocker.mapper.ViewEventToOutput
import io.reactivex.functions.Consumer

class BlockerInteractor(
    savedInstanceState: Bundle?,
    private val output: Consumer<Blocker.Output>
) : Interactor<BlockerView>(
    savedInstanceState = savedInstanceState,
    disposables = null
) {

    override fun onViewCreated(view: BlockerView, viewLifecycle: Lifecycle) {
        viewLifecycle.startStop {
            bind(view to output using ViewEventToOutput)
        }
    }
}
