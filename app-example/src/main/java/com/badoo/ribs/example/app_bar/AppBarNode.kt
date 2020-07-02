package com.badoo.ribs.example.app_bar

import android.view.ViewGroup
import com.badoo.ribs.clienthelper.connector.Connectable
import com.badoo.ribs.clienthelper.connector.NodeConnector
import com.badoo.ribs.core.Node
import com.badoo.ribs.core.modality.BuildParams
import com.badoo.ribs.core.plugin.Plugin
import com.badoo.ribs.example.app_bar.AppBar.Output

class AppBarNode internal constructor(
    buildParams: BuildParams<*>,
    viewFactory: ((ViewGroup) -> AppBarView?)?,
    plugins: List<Plugin> = emptyList(),
    connector: NodeConnector<Nothing, Output> = NodeConnector()
) : Node<AppBarView>(
    buildParams = buildParams,
    viewFactory = viewFactory,
    plugins = plugins
), AppBar, Connectable<Nothing, Output> by connector
