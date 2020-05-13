package com.badoo.ribs.sandbox.rib.hello_world

import android.util.Log
import android.view.ViewGroup
import com.badoo.ribs.core.Node
import com.badoo.ribs.core.builder.BuildParams
import io.reactivex.Single

class HelloWorldNode(
    viewFactory: ((ViewGroup) -> HelloWorldView?)?,
    private val router: HelloWorldRouter,
    interactor: HelloWorldInteractor,
    buildParams: BuildParams<*>
) : Node<HelloWorldView>(
    buildParams = buildParams,
    viewFactory = viewFactory,
    router = router,
    interactor = interactor
), HelloWorld {

    override fun somethingSomethingDarkSide(): Single<HelloWorld> =
        executeWorkflow {
            Log.d("WORKFLOW", "Hello world / somethingSomethingDarkSide")
        }
}
