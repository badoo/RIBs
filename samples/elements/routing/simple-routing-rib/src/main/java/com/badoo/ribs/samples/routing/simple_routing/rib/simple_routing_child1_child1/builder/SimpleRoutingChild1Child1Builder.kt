package com.badoo.ribs.samples.routing.simple_routing.rib.simple_routing_child1_child1.builder

import com.badoo.ribs.builder.SimpleBuilder
import com.badoo.ribs.core.modality.BuildParams
import com.badoo.ribs.samples.routing.simple_routing.rib.simple_routing_child1_child1.SimpleRoutingChild1Child1
import com.badoo.ribs.samples.routing.simple_routing.rib.simple_routing_child1_child1.SimpleRoutingChild1Child1Node

class SimpleRoutingChild1Child1Builder(
    private val dependency: SimpleRoutingChild1Child1.Dependency
) : SimpleBuilder<SimpleRoutingChild1Child1>() {

    override fun build(buildParams: BuildParams<Nothing?>): SimpleRoutingChild1Child1 {
        val customisation = buildParams.getOrDefault(SimpleRoutingChild1Child1.Customisation())
        return SimpleRoutingChild1Child1Node(
            buildParams = buildParams,
            viewFactory = customisation.viewFactory(null)
        )
    }
}
