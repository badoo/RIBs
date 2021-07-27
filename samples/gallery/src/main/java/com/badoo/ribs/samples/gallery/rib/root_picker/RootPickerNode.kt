package com.badoo.ribs.samples.gallery.rib.root_picker

import com.badoo.ribs.rx2.clienthelper.connector.Connectable
import com.badoo.ribs.rx2.clienthelper.connector.NodeConnector
import com.badoo.ribs.core.modality.BuildParams
import com.badoo.ribs.core.plugin.Plugin
import com.badoo.ribs.core.view.ViewFactory
import com.badoo.ribs.samples.gallery.rib.root_picker.RootPicker.Input
import com.badoo.ribs.samples.gallery.rib.root_picker.RootPicker.Output
import com.badoo.ribs.rx2.workflows.RxWorkflowNode

class RootPickerNode internal constructor(
    buildParams: BuildParams<*>,
    viewFactory: ViewFactory<RootPickerView>?,
    plugins: List<Plugin> = emptyList(),
    connector: NodeConnector<Input, Output> = NodeConnector()
) : RxWorkflowNode<RootPickerView>(
    buildParams = buildParams,
    viewFactory = viewFactory,
    plugins = plugins
), RootPicker, Connectable<Input, Output> by connector {

}
