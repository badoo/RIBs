package com.badoo.ribs.samples.android.dialogs.rib.dummy

import com.badoo.ribs.clienthelper.connector.Connectable
import com.badoo.ribs.clienthelper.connector.NodeConnector
import com.badoo.ribs.core.Node
import com.badoo.ribs.core.modality.BuildParams
import com.badoo.ribs.core.plugin.Plugin
import com.badoo.ribs.core.view.ViewFactory
import com.badoo.ribs.samples.android.dialogs.rib.dummy.Dummy.Input
import com.badoo.ribs.samples.android.dialogs.rib.dummy.Dummy.Output

class DummyNode(
    buildParams: BuildParams<Nothing?>,
    viewFactory: ViewFactory<DummyView>,
    plugins: List<Plugin> = emptyList(),
    connector: NodeConnector<Input, Output> = NodeConnector()
) : Node<DummyView>(
    buildParams = buildParams,
    viewFactory = viewFactory,
    plugins = plugins
), Dummy, Connectable<Input, Output> by connector
