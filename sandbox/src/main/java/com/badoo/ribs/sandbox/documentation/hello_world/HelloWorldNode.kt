package com.badoo.ribs.sandbox.documentation.hello_world

import android.util.Log
import com.badoo.ribs.core.Node
import com.badoo.ribs.core.modality.BuildParams
import com.badoo.ribs.core.plugin.Plugin
import com.badoo.ribs.core.view.RibView

internal class HelloWorldNode(
    viewFactory: ((RibView) -> HelloWorldView?)?,
    plugins: List<Plugin> // <--
) : Node<HelloWorldView>(
    buildParams = BuildParams.Empty(),
    viewFactory = viewFactory,
    plugins = plugins // <--
), HelloWorld {

    // Also let's remove this, this is no longer needed:
    // override fun onCreate() {
    //     super.onCreate()
    //     Log.d("RIBs","Hello world!")
    // }
}


// internal constructor
