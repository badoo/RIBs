package com.badoo.ribs.samples.simplerouting.rib.simple_routing_child1_child2.builder

import com.badoo.ribs.builder.SimpleBuilder
import com.badoo.ribs.core.modality.BuildParams
import com.badoo.ribs.samples.simplerouting.rib.simple_routing_child1_child2.SimpleRoutingChild1Child2
import com.badoo.ribs.samples.simplerouting.rib.simple_routing_child1_child2.SimpleRoutingChild1Child2Node

class SimpleRoutingChild1Child2Builder(
    private val dependency: SimpleRoutingChild1Child2.Dependency
) : SimpleBuilder<SimpleRoutingChild1Child2>() {

    override fun build(buildParams: BuildParams<Nothing?>): SimpleRoutingChild1Child2 {
        val customisation = buildParams.getOrDefault(SimpleRoutingChild1Child2.Customisation())
        return SimpleRoutingChild1Child2Node(
            buildParams = buildParams,
            viewFactory = customisation.viewFactory(null)
        )
    }
}
