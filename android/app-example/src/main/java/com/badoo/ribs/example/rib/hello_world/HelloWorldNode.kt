package com.badoo.ribs.example.rib.hello_world

import com.badoo.ribs.core.BuildContext
import android.util.Log
import android.view.ViewGroup
import com.badoo.ribs.core.Node
import io.reactivex.Single

class HelloWorldNode(
    viewFactory: ((ViewGroup) -> HelloWorldView?)?,
    private val router: HelloWorldRouter,
    interactor: HelloWorldInteractor,
    buildContext: BuildContext.Resolved<*>
) : Node<HelloWorldView>(
    buildContext = buildContext,
    viewFactory = viewFactory,
    router = router,
    interactor = interactor
), HelloWorld.Workflow {

    override fun somethingSomethingDarkSide(): Single<HelloWorld.Workflow> =
        executeWorkflow {
            Log.d("WORKFLOW", "Hello world / somethingSomethingDarkSide")
        }
}
