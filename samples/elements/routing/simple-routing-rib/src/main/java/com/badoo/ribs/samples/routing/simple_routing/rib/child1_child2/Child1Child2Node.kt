package com.badoo.ribs.samples.routing.simple_routing.rib.child1_child2

import com.badoo.ribs.core.Node
import com.badoo.ribs.core.modality.BuildParams
import com.badoo.ribs.core.view.ViewFactory

internal class Child1Child2Node(
    buildParams: BuildParams<*>,
    viewFactory: ViewFactory<Child1Child2View>?
) : Node<Child1Child2View>(
    buildParams = buildParams,
    viewFactory = viewFactory
), Child1Child2
