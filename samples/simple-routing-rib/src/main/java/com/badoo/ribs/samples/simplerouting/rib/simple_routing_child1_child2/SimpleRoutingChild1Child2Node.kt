package com.badoo.ribs.samples.simplerouting.rib.simple_routing_child1_child2

import com.badoo.ribs.core.Node
import com.badoo.ribs.core.modality.BuildParams
import com.badoo.ribs.core.view.ViewFactory

internal class SimpleRoutingChild1Child2Node(
    buildParams: BuildParams<*>,
    viewFactory: ViewFactory<SimpleRoutingChild1Child2View>?
) : Node<SimpleRoutingChild1Child2View>(
    buildParams = buildParams,
    viewFactory = viewFactory
), SimpleRoutingChild1Child2
