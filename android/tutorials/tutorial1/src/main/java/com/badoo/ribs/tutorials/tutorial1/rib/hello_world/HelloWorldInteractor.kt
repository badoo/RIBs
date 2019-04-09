package com.badoo.ribs.tutorials.tutorial1.rib.hello_world

import android.arch.lifecycle.Lifecycle
import com.badoo.mvicore.android.lifecycle.createDestroy
import com.badoo.mvicore.binder.using
import com.badoo.ribs.core.Interactor
import com.badoo.ribs.core.Router
import com.badoo.ribs.tutorials.tutorial1.rib.hello_world.HelloWorldRouter.Configuration
import com.badoo.ribs.tutorials.tutorial1.rib.hello_world.mapper.ViewEventToOutput
import io.reactivex.functions.Consumer

class HelloWorldInteractor(
    router: Router<Configuration, HelloWorldView>,
    private val output: Consumer<HelloWorld.Output>
) : Interactor<Configuration, HelloWorldView>(
    router = router,
    disposables = null
) {

    override fun onViewCreated(view: HelloWorldView, viewLifecycle: Lifecycle) {
        viewLifecycle.createDestroy {
            bind(view to output using ViewEventToOutput)
        }
    }
}
