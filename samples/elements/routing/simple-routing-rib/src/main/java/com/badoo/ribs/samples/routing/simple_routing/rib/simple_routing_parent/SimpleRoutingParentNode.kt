package com.badoo.ribs.samples.routing.simple_routing.rib.simple_routing_parent

import com.badoo.ribs.core.Node
import com.badoo.ribs.core.modality.BuildParams
import com.badoo.ribs.core.plugin.Plugin
import com.badoo.ribs.core.view.ViewFactory

internal class SimpleRoutingParentNode(
    buildParams: BuildParams<*>,
    viewFactory: ViewFactory<SimpleRoutingParentView>?,
    plugins: List<Plugin>
) : Node<SimpleRoutingParentView>(
    buildParams = buildParams,
    viewFactory = viewFactory,
    plugins = plugins
), SimpleRoutingParent
