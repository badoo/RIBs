package com.badoo.ribs.tutorials.tutorial1.rib.hello_world

import androidx.lifecycle.Lifecycle
import com.badoo.mvicore.android.lifecycle.startStop
import com.badoo.mvicore.binder.using
import com.badoo.ribs.clienthelper.interactor.Interactor
import com.badoo.ribs.core.builder.BuildParams
import com.badoo.ribs.tutorials.tutorial1.rib.hello_world.mapper.ViewEventToOutput
import io.reactivex.functions.Consumer

class HelloWorldInteractor(
    buildParams: BuildParams<Nothing?>,
    private val output: Consumer<HelloWorld.Output>
) : Interactor<HelloWorld, HelloWorldView>(
    buildParams = buildParams,
    disposables = null
) {

    override fun onViewCreated(view: HelloWorldView, viewLifecycle: Lifecycle) {
        viewLifecycle.startStop {
            bind(view to output using ViewEventToOutput)
        }
    }
}
