package com.badoo.ribs.example.rib.main_hello_world

import android.os.Bundle
import android.util.Log
import android.view.ViewGroup
import com.badoo.ribs.core.Node
import io.reactivex.Single

class MainHelloWorldNode(
    viewFactory: ((ViewGroup) -> MainHelloWorldView?)?,
    private val router: MainHelloWorldRouter,
    interactor: MainHelloWorldInteractor,
    savedInstanceState: Bundle?
) : Node<MainHelloWorldView>(
    savedInstanceState = savedInstanceState,
    identifier = object : MainHelloWorld {},
    viewFactory = viewFactory,
    router = router,
    interactor = interactor
), MainHelloWorld.Workflow {

    override fun somethingSomethingDarkSide(): Single<MainHelloWorld.Workflow> =
        executeWorkflow {
            Log.d("WORKFLOW", "Hello world / somethingSomethingDarkSide")
        }
}
