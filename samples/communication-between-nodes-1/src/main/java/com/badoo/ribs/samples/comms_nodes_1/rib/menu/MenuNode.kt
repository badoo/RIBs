package com.badoo.ribs.samples.comms_nodes_1.rib.menu

import com.badoo.ribs.core.Node
import com.badoo.ribs.core.modality.BuildParams
import com.badoo.ribs.core.plugin.Plugin
import com.badoo.ribs.core.view.RibView

class MenuNode internal constructor(
    buildParams: BuildParams<*>,
    viewFactory: ((RibView) -> MenuView?)?,
    plugins: List<Plugin> = emptyList()
) : Node<MenuView>(
    buildParams = buildParams,
    viewFactory = viewFactory,
    plugins = plugins
), Menu {
}
