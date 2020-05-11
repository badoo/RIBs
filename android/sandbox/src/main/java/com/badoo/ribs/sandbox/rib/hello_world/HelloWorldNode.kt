package com.badoo.ribs.sandbox.rib.hello_world

import android.util.Log
import android.view.ViewGroup
import com.badoo.ribs.core.Node
import com.badoo.ribs.core.builder.BuildParams
import com.badoo.ribs.core.plugin.Plugin
import io.reactivex.Single

class HelloWorldNode(
    viewFactory: ((ViewGroup) -> HelloWorldView?)?,
    buildParams: BuildParams<*>,
    plugins: List<Plugin> = emptyList()
) : Node<HelloWorldView>(
    buildParams = buildParams,
    viewFactory = viewFactory,
    plugins = plugins
), HelloWorld {

    override fun somethingSomethingDarkSide(): Single<HelloWorld> =
        executeWorkflow {
            Log.d("WORKFLOW", "Hello world / somethingSomethingDarkSide")
        }
}
