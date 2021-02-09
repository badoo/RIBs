package com.badoo.ribs.samples.comms_nodes_1.rib.container

import com.badoo.ribs.core.Node
import com.badoo.ribs.core.modality.BuildParams
import com.badoo.ribs.core.plugin.Plugin
import com.badoo.ribs.core.view.RibView

class ContainerNode internal constructor(
    buildParams: BuildParams<*>,
    viewFactory: ((RibView) -> ContainerView?)?,
    plugins: List<Plugin> = emptyList()
) : Node<ContainerView>(
    buildParams = buildParams,
    viewFactory = viewFactory,
    plugins = plugins
), Container {
}
