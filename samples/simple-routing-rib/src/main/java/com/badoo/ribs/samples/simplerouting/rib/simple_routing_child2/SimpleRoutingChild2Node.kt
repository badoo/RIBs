package com.badoo.ribs.samples.simplerouting.rib.simple_routing_child2

import com.badoo.ribs.core.Node
import com.badoo.ribs.core.modality.BuildParams
import com.badoo.ribs.core.plugin.Plugin
import com.badoo.ribs.core.view.RibView

internal class SimpleRoutingChild2Node(
    buildParams: BuildParams<*>,
    viewFactory: ((RibView) -> SimpleRoutingChild2View?)?,
    plugins: List<Plugin>
) : Node<SimpleRoutingChild2View>(
    buildParams = buildParams,
    viewFactory = viewFactory,
    plugins = plugins
), SimpleRoutingChild2
