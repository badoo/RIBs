package com.samples.helloworld.hello_world

import com.badoo.ribs.core.Node
import com.badoo.ribs.core.modality.BuildParams
import com.badoo.ribs.core.plugin.Plugin
import com.badoo.ribs.core.view.RibView

internal class HelloWorldNode(
    viewFactory: ((RibView) -> HelloWorldView?)?,
    plugins: List<Plugin>
) : Node<HelloWorldView>(
    buildParams = BuildParams.Empty(),
    viewFactory = viewFactory,
    plugins = plugins
), HelloWorld
