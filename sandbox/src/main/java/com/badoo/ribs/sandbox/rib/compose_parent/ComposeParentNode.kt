package com.badoo.ribs.sandbox.rib.compose_parent

import com.badoo.ribs.clienthelper.connector.Connectable
import com.badoo.ribs.clienthelper.connector.NodeConnector
import com.badoo.ribs.core.Node
import com.badoo.ribs.core.modality.BuildParams
import com.badoo.ribs.core.plugin.Plugin
import com.badoo.ribs.core.view.RibView
import com.badoo.ribs.sandbox.rib.compose_parent.ComposeParent.Input
import com.badoo.ribs.sandbox.rib.compose_parent.ComposeParent.Output

class ComposeParentNode internal constructor(
    buildParams: BuildParams<*>,
    viewFactory: ((RibView) -> ComposeParentView?)?,
    plugins: List<Plugin> = emptyList(),
    connector: NodeConnector<Input, Output> = NodeConnector()
) : Node<ComposeParentView>(
    buildParams = buildParams,
    viewFactory = viewFactory,
    plugins = plugins
), ComposeParent, Connectable<Input, Output> by connector {

}
