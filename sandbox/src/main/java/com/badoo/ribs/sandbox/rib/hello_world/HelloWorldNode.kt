package com.badoo.ribs.sandbox.rib.hello_world

import android.util.Log
import com.badoo.ribs.clienthelper.connector.Connectable
import com.badoo.ribs.clienthelper.connector.NodeConnector
import com.badoo.ribs.core.Node
import com.badoo.ribs.core.modality.BuildParams
import com.badoo.ribs.core.plugin.Plugin
import com.badoo.ribs.core.state.rx2
import com.badoo.ribs.core.view.RibView
import com.badoo.ribs.sandbox.rib.hello_world.HelloWorld.Input
import com.badoo.ribs.sandbox.rib.hello_world.HelloWorld.Output
import io.reactivex.Single

class HelloWorldNode(
    buildParams: BuildParams<*>,
    viewFactory: ((RibView) -> HelloWorldView?)?,
    plugins: List<Plugin> = emptyList(),
    connector: NodeConnector<Input, Output> = NodeConnector()
) : Node<HelloWorldView>(
    buildParams = buildParams,
    viewFactory = viewFactory,
    plugins = plugins
), HelloWorld, Connectable<Input, Output> by connector {

    override fun somethingSomethingDarkSide(): Single<HelloWorld> =
        executeWorkflow<HelloWorld> {
            Log.d("WORKFLOW", "Hello world / somethingSomethingDarkSide")
        }.rx2()
}
