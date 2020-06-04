package com.badoo.ribs.sandbox.rib.dialog_example

import android.view.ViewGroup
import com.badoo.ribs.clienthelper.connector.Connectable
import com.badoo.ribs.clienthelper.connector.NodeConnector
import com.badoo.ribs.core.Node
import com.badoo.ribs.core.modality.BuildParams
import com.badoo.ribs.core.plugin.Plugin
import com.badoo.ribs.sandbox.rib.dialog_example.DialogExample.Input
import com.badoo.ribs.sandbox.rib.dialog_example.DialogExample.Output

class DialogExampleNode(
    buildParams: BuildParams<*>,
    viewFactory: ((ViewGroup) -> DialogExampleView?)?,
    plugins: List<Plugin> = emptyList(),
    connector: NodeConnector<Input, Output> = NodeConnector()
) : Node<DialogExampleView>(
    buildParams = buildParams,
    viewFactory = viewFactory,
    plugins = plugins
), DialogExample, Connectable<Input, Output> by connector
