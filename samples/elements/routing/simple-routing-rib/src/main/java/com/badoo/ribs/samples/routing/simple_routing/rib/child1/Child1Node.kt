package com.badoo.ribs.samples.routing.simple_routing.rib.child1

import com.badoo.ribs.core.Node
import com.badoo.ribs.core.modality.BuildParams
import com.badoo.ribs.core.plugin.Plugin
import com.badoo.ribs.core.view.ViewFactory

internal class Child1Node(
    buildParams: BuildParams<*>,
    viewFactory: ViewFactory<Child1View>?,
    plugins: List<Plugin>
) : Node<Child1View>(
    buildParams = buildParams,
    viewFactory = viewFactory,
    plugins = plugins
), Child1
