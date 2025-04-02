package com.badoo.ribs.samples.gallery.rib.communication.picker

import com.badoo.ribs.core.modality.BuildParams
import com.badoo.ribs.core.plugin.Plugin
import com.badoo.ribs.core.view.ViewFactory
import com.badoo.ribs.rx3.clienthelper.connector.Connectable
import com.badoo.ribs.rx3.clienthelper.connector.NodeConnector
import com.badoo.ribs.rx3.workflows.RxWorkflowNode
import com.badoo.ribs.samples.gallery.rib.communication.picker.CommunicationPicker.Input
import com.badoo.ribs.samples.gallery.rib.communication.picker.CommunicationPicker.Output

class CommunicationPickerNode internal constructor(
    buildParams: BuildParams<*>,
    viewFactory: ViewFactory<CommunicationPickerView>?,
    plugins: List<Plugin> = emptyList(),
    connector: NodeConnector<Input, Output> = NodeConnector()
) : RxWorkflowNode<CommunicationPickerView>(
    buildParams = buildParams,
    viewFactory = viewFactory,
    plugins = plugins
), CommunicationPicker, Connectable<Input, Output> by connector
