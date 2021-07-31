package com.badoo.ribs.samples.routing.parameterised_routing.rib.parameterised_routing_example

import com.badoo.ribs.core.Node
import com.badoo.ribs.core.modality.BuildParams
import com.badoo.ribs.core.plugin.Plugin
import com.badoo.ribs.core.view.ViewFactory

class ParameterisedRoutingExampleNode internal constructor(
    buildParams: BuildParams<*>,
    viewFactory: ViewFactory<ParameterisedRoutingExampleView>?,
    plugins: List<Plugin> = emptyList()
) : Node<ParameterisedRoutingExampleView>(
    buildParams = buildParams,
    viewFactory = viewFactory,
    plugins = plugins
), ParameterisedRoutingExample
