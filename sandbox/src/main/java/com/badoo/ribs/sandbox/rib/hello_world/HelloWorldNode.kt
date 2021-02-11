package com.badoo.ribs.sandbox.rib.hello_world

import android.util.Log
import com.badoo.ribs.rx.clienthelper.connector.Connectable
import com.badoo.ribs.rx.clienthelper.connector.NodeConnector
import com.badoo.ribs.core.modality.BuildParams
import com.badoo.ribs.core.plugin.Plugin
import com.badoo.ribs.core.view.RibView
import com.badoo.ribs.sandbox.rib.hello_world.HelloWorld.Input
import com.badoo.ribs.sandbox.rib.hello_world.HelloWorld.Output
import com.badoo.ribs.rx.workflows.RxWorkflowNode
import io.reactivex.Single

class HelloWorldNode(
    buildParams: BuildParams<*>,
    viewFactory: ((RibView) -> HelloWorldView?)?,
    plugins: List<Plugin> = emptyList(),
    connector: NodeConnector<Input, Output> = NodeConnector()
) : RxWorkflowNode<HelloWorldView>(
    buildParams = buildParams,
    viewFactory = viewFactory,
    plugins = plugins
), HelloWorld, Connectable<Input, Output> by connector {

    override fun somethingSomethingDarkSide(): Single<HelloWorld> =
        executeWorkflow {
            Log.d("WORKFLOW", "Hello world / somethingSomethingDarkSide")
        }
}
