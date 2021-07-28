package com.badoo.ribs.samples.gallery.rib.other_container

import com.badoo.ribs.core.modality.BuildParams
import com.badoo.ribs.core.plugin.Plugin
import com.badoo.ribs.core.view.ViewFactory
import com.badoo.ribs.rx2.clienthelper.connector.Connectable
import com.badoo.ribs.rx2.clienthelper.connector.NodeConnector
import com.badoo.ribs.rx2.workflows.RxWorkflowNode
import com.badoo.ribs.samples.gallery.rib.other_container.OtherContainer.Input
import com.badoo.ribs.samples.gallery.rib.other_container.OtherContainer.Output

class OtherContainerNode internal constructor(
    buildParams: BuildParams<*>,
    plugins: List<Plugin> = emptyList(),
    viewFactory: ViewFactory<OtherContainerView>,
    connector: NodeConnector<Input, Output> = NodeConnector(),
) : RxWorkflowNode<OtherContainerView>(
    buildParams = buildParams,
    viewFactory = viewFactory,
    plugins = plugins
), OtherContainer, Connectable<Input, Output> by connector {

}
