package com.badoo.ribs.samples.gallery.rib.communication_container

import com.badoo.ribs.core.modality.BuildParams
import com.badoo.ribs.core.plugin.Plugin
import com.badoo.ribs.core.view.ViewFactory
import com.badoo.ribs.rx2.clienthelper.connector.Connectable
import com.badoo.ribs.rx2.clienthelper.connector.NodeConnector
import com.badoo.ribs.rx2.workflows.RxWorkflowNode
import com.badoo.ribs.samples.gallery.rib.communication_container.CommunicationContainer.Input
import com.badoo.ribs.samples.gallery.rib.communication_container.CommunicationContainer.Output

class CommunicationContainerNode internal constructor(
    buildParams: BuildParams<*>,
    plugins: List<Plugin> = emptyList(),
    viewFactory: ViewFactory<CommunicationContainerView>,
    connector: NodeConnector<Input, Output> = NodeConnector(),
) : RxWorkflowNode<CommunicationContainerView>(
    buildParams = buildParams,
    viewFactory = viewFactory,
    plugins = plugins
), CommunicationContainer, Connectable<Input, Output> by connector {

}
