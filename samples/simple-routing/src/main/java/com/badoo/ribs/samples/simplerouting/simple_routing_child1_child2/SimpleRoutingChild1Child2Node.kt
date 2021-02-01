package com.badoo.ribs.samples.simplerouting.simple_routing_child1_child2

import com.badoo.ribs.core.Node
import com.badoo.ribs.core.modality.BuildParams
import com.badoo.ribs.core.view.RibView

internal class SimpleRoutingChild1Child2Node(
    buildParams: BuildParams<*>,
    viewFactory: ((RibView) -> SimpleRoutingChild1Child2View?)?
) : Node<SimpleRoutingChild1Child2View>(
    buildParams = buildParams,
    viewFactory = viewFactory
), SimpleRoutingChild1Child2
