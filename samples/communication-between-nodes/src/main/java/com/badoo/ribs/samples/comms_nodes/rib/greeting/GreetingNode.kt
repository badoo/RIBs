package com.badoo.ribs.samples.comms_nodes.rib.greeting

import com.badoo.ribs.clienthelper.connector.Connectable
import com.badoo.ribs.clienthelper.connector.NodeConnector
import com.badoo.ribs.core.Node
import com.badoo.ribs.core.modality.BuildParams
import com.badoo.ribs.core.plugin.Plugin
import com.badoo.ribs.core.view.ViewFactory
import com.badoo.ribs.samples.comms_nodes.rib.greeting.Greeting.Input
import com.badoo.ribs.samples.comms_nodes.rib.greeting.Greeting.Output

class GreetingNode internal constructor(
    buildParams: BuildParams<*>,
    viewFactory: ViewFactory<GreetingView>?,
    plugins: List<Plugin> = emptyList(),
    connector: NodeConnector<Input, Output> = NodeConnector()
) : Node<GreetingView>(
    buildParams = buildParams,
    viewFactory = viewFactory,
    plugins = plugins
), Greeting, Connectable<Input, Output> by connector
