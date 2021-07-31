package com.badoo.ribs.samples.gallery.rib.routing.routing_container

import com.badoo.ribs.core.modality.BuildParams
import com.badoo.ribs.core.plugin.Plugin
import com.badoo.ribs.core.view.ViewFactory
import com.badoo.ribs.rx2.clienthelper.connector.Connectable
import com.badoo.ribs.rx2.clienthelper.connector.NodeConnector
import com.badoo.ribs.rx2.workflows.RxWorkflowNode
import com.badoo.ribs.samples.gallery.rib.routing.routing_container.RoutingContainer.Input
import com.badoo.ribs.samples.gallery.rib.routing.routing_container.RoutingContainer.Output

class RoutingContainerNode internal constructor(
    buildParams: BuildParams<*>,
    plugins: List<Plugin> = emptyList(),
    viewFactory: ViewFactory<RoutingContainerView>,
    connector: NodeConnector<Input, Output> = NodeConnector(),
) : RxWorkflowNode<RoutingContainerView>(
    buildParams = buildParams,
    viewFactory = viewFactory,
    plugins = plugins
), RoutingContainer, Connectable<Input, Output> by connector {

}
