package com.badoo.ribs.samples.routing.simple_routing.rib.child2

import com.badoo.ribs.core.Node
import com.badoo.ribs.core.modality.BuildParams
import com.badoo.ribs.core.plugin.Plugin
import com.badoo.ribs.core.view.ViewFactory

internal class Child2Node(
    buildParams: BuildParams<*>,
    viewFactory: ViewFactory<Child2View>?,
    plugins: List<Plugin>
) : Node<Child2View>(
    buildParams = buildParams,
    viewFactory = viewFactory,
    plugins = plugins
), Child2
