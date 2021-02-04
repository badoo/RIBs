package com.badoo.ribs.samples.simplerouting.rib.simple_routing_child1

import com.badoo.ribs.core.Node
import com.badoo.ribs.core.modality.BuildParams
import com.badoo.ribs.core.plugin.Plugin
import com.badoo.ribs.core.view.RibView

internal class SimpleRoutingChild1Node(
        buildParams: BuildParams<*>,
        viewFactory: ((RibView) -> SimpleRoutingChild1View?)?,
        plugins: List<Plugin>
) : Node<SimpleRoutingChild1View>(
    buildParams = buildParams,
    viewFactory = viewFactory,
    plugins = plugins
), SimpleRoutingChild1
