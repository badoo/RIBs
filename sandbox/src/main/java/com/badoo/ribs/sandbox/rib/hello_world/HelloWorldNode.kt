package com.badoo.ribs.sandbox.rib.hello_world

import android.util.Log
import com.badoo.ribs.core.modality.BuildParams
import com.badoo.ribs.core.plugin.Plugin
import com.badoo.ribs.core.view.ViewFactory
import com.badoo.ribs.rx3.clienthelper.connector.Connectable
import com.badoo.ribs.rx3.clienthelper.connector.NodeConnector
import com.badoo.ribs.rx3.workflows.RxWorkflowNode
import com.badoo.ribs.sandbox.rib.hello_world.HelloWorld.Input
import com.badoo.ribs.sandbox.rib.hello_world.HelloWorld.Output
import io.reactivex.rxjava3.core.Single

class HelloWorldNode(
    buildParams: BuildParams<*>,
    viewFactory: ViewFactory<HelloWorldView>?,
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
