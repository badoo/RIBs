package com.badoo.ribs.samples.routing.simple_routing.rib.simple_routing_child1_child1

import com.badoo.ribs.core.Node
import com.badoo.ribs.core.modality.BuildParams
import com.badoo.ribs.core.view.ViewFactory

internal class SimpleRoutingChild1Child1Node(
    buildParams: BuildParams<*>,
    viewFactory: ViewFactory<SimpleRoutingChild1Child1View>?
) : Node<SimpleRoutingChild1Child1View>(
    buildParams = buildParams,
    viewFactory = viewFactory
), SimpleRoutingChild1Child1
