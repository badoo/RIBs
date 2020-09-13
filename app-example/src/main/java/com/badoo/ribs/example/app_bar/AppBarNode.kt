package com.badoo.ribs.example.app_bar

import com.badoo.ribs.clienthelper.connector.Connectable
import com.badoo.ribs.clienthelper.connector.NodeConnector
import com.badoo.ribs.core.Node
import com.badoo.ribs.core.modality.BuildParams
import com.badoo.ribs.core.plugin.Plugin
import com.badoo.ribs.core.view.RibView
import com.badoo.ribs.example.app_bar.AppBar.Input
import com.badoo.ribs.example.app_bar.AppBar.Output

class AppBarNode internal constructor(
    buildParams: BuildParams<*>,
    viewFactory: ((RibView) -> AppBarView?)?,
    plugins: List<Plugin> = emptyList(),
    connector: NodeConnector<Input, Output> = NodeConnector()
) : Node<AppBarView>(
    buildParams = buildParams,
    viewFactory = viewFactory,
    plugins = plugins
), AppBar, Connectable<Input, Output> by connector
