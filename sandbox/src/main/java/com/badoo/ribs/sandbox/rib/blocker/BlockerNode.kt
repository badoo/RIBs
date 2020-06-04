package com.badoo.ribs.sandbox.rib.blocker

import android.view.ViewGroup
import com.badoo.ribs.clienthelper.connector.Connectable
import com.badoo.ribs.clienthelper.connector.NodeConnector
import com.badoo.ribs.core.Node
import com.badoo.ribs.core.builder.BuildParams
import com.badoo.ribs.core.plugin.Plugin

class BlockerNode(
    buildParams: BuildParams<Nothing?>,
    viewFactory: (ViewGroup) -> BlockerView,
    plugins: List<Plugin> = emptyList(),
    val connector: NodeConnector<Blocker.Input, Blocker.Output> = NodeConnector()
): Node<BlockerView>(
    buildParams = buildParams,
    viewFactory = viewFactory,
    plugins = plugins
), Blocker, Connectable<Blocker.Input, Blocker.Output> by connector
