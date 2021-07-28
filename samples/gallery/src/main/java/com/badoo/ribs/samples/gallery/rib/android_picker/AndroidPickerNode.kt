package com.badoo.ribs.samples.gallery.rib.android_picker

import com.badoo.ribs.core.modality.BuildParams
import com.badoo.ribs.core.plugin.Plugin
import com.badoo.ribs.core.view.ViewFactory
import com.badoo.ribs.rx2.clienthelper.connector.Connectable
import com.badoo.ribs.rx2.clienthelper.connector.NodeConnector
import com.badoo.ribs.rx2.workflows.RxWorkflowNode
import com.badoo.ribs.samples.gallery.rib.android_picker.AndroidPicker.Input
import com.badoo.ribs.samples.gallery.rib.android_picker.AndroidPicker.Output

class AndroidPickerNode internal constructor(
    buildParams: BuildParams<*>,
    viewFactory: ViewFactory<AndroidPickerView>?,
    plugins: List<Plugin> = emptyList(),
    connector: NodeConnector<Input, Output> = NodeConnector()
) : RxWorkflowNode<AndroidPickerView>(
    buildParams = buildParams,
    viewFactory = viewFactory,
    plugins = plugins
), AndroidPicker, Connectable<Input, Output> by connector {

}
