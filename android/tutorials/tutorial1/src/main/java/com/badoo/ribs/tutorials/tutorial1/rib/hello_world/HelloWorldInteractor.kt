package com.badoo.ribs.tutorials.tutorial1.rib.hello_world

import android.os.Bundle
import androidx.lifecycle.Lifecycle
import com.badoo.mvicore.android.lifecycle.startStop
import com.badoo.mvicore.binder.using
import com.badoo.ribs.core.Interactor
import com.badoo.ribs.tutorials.tutorial1.rib.hello_world.mapper.ViewEventToOutput
import io.reactivex.functions.Consumer

class HelloWorldInteractor(
    savedInstanceState: Bundle?,
    private val output: Consumer<HelloWorld.Output>
) : Interactor<HelloWorldView>(
    savedInstanceState = savedInstanceState,
    disposables = null
) {

    override fun onViewCreated(view: HelloWorldView, viewLifecycle: Lifecycle) {
        viewLifecycle.startStop {
            bind(view to output using ViewEventToOutput)
        }
    }
}
