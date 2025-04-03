package com.badoo.ribs.samples.gallery.rib.communication.container

import com.badoo.ribs.core.modality.BuildParams
import com.badoo.ribs.core.plugin.Plugin
import com.badoo.ribs.core.view.ViewFactory
import com.badoo.ribs.rx3.clienthelper.connector.Connectable
import com.badoo.ribs.rx3.clienthelper.connector.NodeConnector
import com.badoo.ribs.rx3.workflows.RxWorkflowNode
import com.badoo.ribs.samples.gallery.rib.communication.container.CommunicationContainer.Input
import com.badoo.ribs.samples.gallery.rib.communication.container.CommunicationContainer.Output

class CommunicationContainerNode internal constructor(
    buildParams: BuildParams<*>,
    plugins: List<Plugin> = emptyList(),
    viewFactory: ViewFactory<CommunicationContainerView>,
    connector: NodeConnector<Input, Output> = NodeConnector(),
) : RxWorkflowNode<CommunicationContainerView>(
    buildParams = buildParams,
    viewFactory = viewFactory,
    plugins = plugins
), CommunicationContainer, Connectable<Input, Output> by connector
