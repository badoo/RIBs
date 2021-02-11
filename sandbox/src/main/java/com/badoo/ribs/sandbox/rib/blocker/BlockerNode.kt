package com.badoo.ribs.sandbox.rib.blocker

import com.badoo.ribs.rx.clienthelper.connector.Connectable
import com.badoo.ribs.rx.clienthelper.connector.NodeConnector
import com.badoo.ribs.core.Node
import com.badoo.ribs.core.modality.BuildParams
import com.badoo.ribs.core.plugin.Plugin
import com.badoo.ribs.core.view.RibView

class BlockerNode(
    buildParams: BuildParams<Nothing?>,
    viewFactory: (RibView) -> BlockerView,
    plugins: List<Plugin> = emptyList(),
    val connector: NodeConnector<Blocker.Input, Blocker.Output> = NodeConnector()
): Node<BlockerView>(
    buildParams = buildParams,
    viewFactory = viewFactory,
    plugins = plugins
), Blocker, Connectable<Blocker.Input, Blocker.Output> by connector
