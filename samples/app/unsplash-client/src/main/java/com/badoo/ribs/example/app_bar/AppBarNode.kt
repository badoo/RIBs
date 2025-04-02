package com.badoo.ribs.example.app_bar

import com.badoo.ribs.core.Node
import com.badoo.ribs.core.modality.BuildParams
import com.badoo.ribs.core.plugin.Plugin
import com.badoo.ribs.core.view.ViewFactory
import com.badoo.ribs.example.app_bar.AppBar.Input
import com.badoo.ribs.example.app_bar.AppBar.Output
import com.badoo.ribs.rx3.clienthelper.connector.Connectable
import com.badoo.ribs.rx3.clienthelper.connector.NodeConnector

class AppBarNode internal constructor(
    buildParams: BuildParams<*>,
    viewFactory: ViewFactory<AppBarView>?,
    plugins: List<Plugin> = emptyList(),
    connector: NodeConnector<Input, Output> = NodeConnector()
) : Node<AppBarView>(
    buildParams = buildParams,
    viewFactory = viewFactory,
    plugins = plugins
), AppBar, Connectable<Input, Output> by connector
