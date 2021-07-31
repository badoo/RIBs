package com.badoo.ribs.samples.routing.simple_routing.rib.child1_child1

import com.badoo.ribs.core.Node
import com.badoo.ribs.core.modality.BuildParams
import com.badoo.ribs.core.view.ViewFactory

internal class Child1Child1Node(
    buildParams: BuildParams<*>,
    viewFactory: ViewFactory<Child1Child1View>?
) : Node<Child1Child1View>(
    buildParams = buildParams,
    viewFactory = viewFactory
), Child1Child1
