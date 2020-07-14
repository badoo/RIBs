package com.badoo.ribs.sandbox.rib.compose_leaf

import com.badoo.ribs.clienthelper.connector.Connectable
import com.badoo.ribs.clienthelper.connector.NodeConnector
import com.badoo.ribs.core.Node
import com.badoo.ribs.core.modality.BuildParams
import com.badoo.ribs.core.plugin.Plugin
import com.badoo.ribs.core.view.RibView
import com.badoo.ribs.sandbox.rib.compose_leaf.ComposeLeaf.Input
import com.badoo.ribs.sandbox.rib.compose_leaf.ComposeLeaf.Output

class ComposeLeafNode internal constructor(
    buildParams: BuildParams<*>,
    viewFactory: ((RibView) -> ComposeLeafView?)?,
    plugins: List<Plugin> = emptyList(),
    connector: NodeConnector<Input, Output> = NodeConnector()
) : Node<ComposeLeafView>(
    buildParams = buildParams,
    viewFactory = viewFactory,
    plugins = plugins
), ComposeLeaf, Connectable<Input, Output> by connector {

}
