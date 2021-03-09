package com.badoo.ribs.sandbox.rib.lorem_ipsum

import com.badoo.ribs.rx2.clienthelper.connector.Connectable
import com.badoo.ribs.rx2.clienthelper.connector.NodeConnector
import com.badoo.ribs.core.Node
import com.badoo.ribs.core.modality.BuildParams
import com.badoo.ribs.core.plugin.Plugin
import com.badoo.ribs.core.view.ViewFactory
import com.badoo.ribs.sandbox.rib.lorem_ipsum.LoremIpsum.Input
import com.badoo.ribs.sandbox.rib.lorem_ipsum.LoremIpsum.Output

class LoremIpsumNode(
    buildParams: BuildParams<Nothing?>,
    viewFactory: ViewFactory<LoremIpsumView>,
    plugins: List<Plugin> = emptyList(),
    connector: NodeConnector<Input, Output> = NodeConnector()
): Node<LoremIpsumView>(
    buildParams = buildParams,
    viewFactory = viewFactory,
    plugins = plugins
), LoremIpsum, Connectable<Input, Output> by connector {

}
