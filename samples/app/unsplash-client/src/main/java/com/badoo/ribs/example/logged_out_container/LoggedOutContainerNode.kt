package com.badoo.ribs.example.logged_out_container

import com.badoo.ribs.rx2.clienthelper.connector.Connectable
import com.badoo.ribs.rx2.clienthelper.connector.NodeConnector
import com.badoo.ribs.core.Node
import com.badoo.ribs.core.modality.BuildParams
import com.badoo.ribs.core.plugin.Plugin
import com.badoo.ribs.example.logged_out_container.LoggedOutContainer.Input
import com.badoo.ribs.example.logged_out_container.LoggedOutContainer.Output

class LoggedOutContainerNode internal constructor(
    buildParams: BuildParams<*>,
    plugins: List<Plugin> = emptyList(),
    connector: NodeConnector<Input, Output> = NodeConnector()
) : Node<Nothing>(
    buildParams = buildParams,
    viewFactory = null,
    plugins = plugins
), LoggedOutContainer, Connectable<Input, Output> by connector
