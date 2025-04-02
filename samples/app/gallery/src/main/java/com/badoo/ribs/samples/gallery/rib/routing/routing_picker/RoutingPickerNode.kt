package com.badoo.ribs.samples.gallery.rib.routing.routing_picker

import com.badoo.ribs.core.modality.BuildParams
import com.badoo.ribs.core.plugin.Plugin
import com.badoo.ribs.core.view.ViewFactory
import com.badoo.ribs.rx3.clienthelper.connector.Connectable
import com.badoo.ribs.rx3.clienthelper.connector.NodeConnector
import com.badoo.ribs.rx3.workflows.RxWorkflowNode
import com.badoo.ribs.samples.gallery.rib.routing.routing_picker.RoutingPicker.Input
import com.badoo.ribs.samples.gallery.rib.routing.routing_picker.RoutingPicker.Output

class RoutingPickerNode internal constructor(
    buildParams: BuildParams<*>,
    viewFactory: ViewFactory<RoutingPickerView>?,
    plugins: List<Plugin> = emptyList(),
    connector: NodeConnector<Input, Output> = NodeConnector()
) : RxWorkflowNode<RoutingPickerView>(
    buildParams = buildParams,
    viewFactory = viewFactory,
    plugins = plugins
), RoutingPicker, Connectable<Input, Output> by connector
