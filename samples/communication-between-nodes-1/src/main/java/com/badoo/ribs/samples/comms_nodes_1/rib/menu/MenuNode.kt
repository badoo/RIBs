package com.badoo.ribs.samples.comms_nodes_1.rib.menu

import com.badoo.ribs.clienthelper.connector.Connectable
import com.badoo.ribs.clienthelper.connector.NodeConnector
import com.badoo.ribs.core.Node
import com.badoo.ribs.core.modality.BuildParams
import com.badoo.ribs.core.plugin.Plugin
import com.badoo.ribs.core.view.RibView
import com.badoo.ribs.samples.comms_nodes_1.rib.menu.Menu.Input
import com.badoo.ribs.samples.comms_nodes_1.rib.menu.Menu.Output

class MenuNode internal constructor(
    buildParams: BuildParams<*>,
    viewFactory: ((RibView) -> MenuView?)?,
    plugins: List<Plugin> = emptyList(),
    connector: NodeConnector<Input, Output> = NodeConnector()
) : Node<MenuView>(
    buildParams = buildParams,
    viewFactory = viewFactory,
    plugins = plugins
), Menu, Connectable<Input, Output> by connector
