package com.badoo.ribs.samples.routing.simple_routing.rib.simple_routing_child1

import com.badoo.ribs.core.Node
import com.badoo.ribs.core.modality.BuildParams
import com.badoo.ribs.core.plugin.Plugin
import com.badoo.ribs.core.view.ViewFactory

internal class SimpleRoutingChild1Node(
    buildParams: BuildParams<*>,
    viewFactory: ViewFactory<SimpleRoutingChild1View>?,
    plugins: List<Plugin>
) : Node<SimpleRoutingChild1View>(
    buildParams = buildParams,
    viewFactory = viewFactory,
    plugins = plugins
), SimpleRoutingChild1
