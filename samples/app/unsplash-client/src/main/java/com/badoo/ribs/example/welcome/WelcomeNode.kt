package com.badoo.ribs.example.welcome

import com.badoo.ribs.rx2.clienthelper.connector.Connectable
import com.badoo.ribs.rx2.clienthelper.connector.NodeConnector
import com.badoo.ribs.core.Node
import com.badoo.ribs.core.modality.BuildParams
import com.badoo.ribs.core.plugin.Plugin
import com.badoo.ribs.core.view.ViewFactory
import com.badoo.ribs.example.welcome.Welcome.Input
import com.badoo.ribs.example.welcome.Welcome.Output

class WelcomeNode internal constructor(
    buildParams: BuildParams<*>,
    viewFactory: ViewFactory<WelcomeView>?,
    plugins: List<Plugin> = emptyList(),
    connector: NodeConnector<Input, Output> = NodeConnector()
) : Node<WelcomeView>(
    buildParams = buildParams,
    viewFactory = viewFactory,
    plugins = plugins
), Welcome, Connectable<Input, Output> by connector
