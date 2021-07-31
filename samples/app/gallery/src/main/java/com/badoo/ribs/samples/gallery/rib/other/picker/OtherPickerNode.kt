package com.badoo.ribs.samples.gallery.rib.other.picker

import com.badoo.ribs.core.modality.BuildParams
import com.badoo.ribs.core.plugin.Plugin
import com.badoo.ribs.core.view.ViewFactory
import com.badoo.ribs.rx2.clienthelper.connector.Connectable
import com.badoo.ribs.rx2.clienthelper.connector.NodeConnector
import com.badoo.ribs.rx2.workflows.RxWorkflowNode
import com.badoo.ribs.samples.gallery.rib.other.picker.OtherPicker.Input
import com.badoo.ribs.samples.gallery.rib.other.picker.OtherPicker.Output

class OtherPickerNode internal constructor(
    buildParams: BuildParams<*>,
    viewFactory: ViewFactory<OtherPickerView>?,
    plugins: List<Plugin> = emptyList(),
    connector: NodeConnector<Input, Output> = NodeConnector()
) : RxWorkflowNode<OtherPickerView>(
    buildParams = buildParams,
    viewFactory = viewFactory,
    plugins = plugins
), OtherPicker, Connectable<Input, Output> by connector {

}
