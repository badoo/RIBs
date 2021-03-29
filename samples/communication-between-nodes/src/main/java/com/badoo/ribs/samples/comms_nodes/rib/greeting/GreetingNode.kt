package com.badoo.ribs.samples.comms_nodes.rib.greeting

import com.badoo.ribs.clienthelper.connector.Connectable
import com.badoo.ribs.clienthelper.connector.NodeConnector
import com.badoo.ribs.core.Node
import com.badoo.ribs.core.modality.BuildParams
import com.badoo.ribs.core.plugin.Plugin
import com.badoo.ribs.core.view.ViewFactory

class GreetingNode internal constructor(
    buildParams: BuildParams<*>,
    viewFactory: ViewFactory<GreetingView>?,
    plugins: List<Plugin> = emptyList(),
    connector: NodeConnector<Greeting.Input, Greeting.Output> = NodeConnector()
) : Node<GreetingView>(
    buildParams = buildParams,
    viewFactory = viewFactory,
    plugins = plugins
), Greeting, Connectable<Greeting.Input, Greeting.Output> by connector
