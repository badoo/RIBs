package com.badoo.ribs.samples.simplerouting.rib.simple_routing_parent

import com.badoo.ribs.core.Node
import com.badoo.ribs.core.modality.BuildParams
import com.badoo.ribs.core.plugin.Plugin
import com.badoo.ribs.core.view.RibView

internal class SimpleRoutingParentNode(
        viewFactory: ((RibView) -> SimpleRoutingParentView?)?,
        plugins: List<Plugin>
) : Node<SimpleRoutingParentView>(
    buildParams = BuildParams.Empty(),
    viewFactory = viewFactory,
    plugins = plugins
), SimpleRoutingParent
