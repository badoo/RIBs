package com.badoo.ribs.samples.simplerouting.rib.simple_routing_child1_child1.builder

import com.badoo.ribs.builder.SimpleBuilder
import com.badoo.ribs.core.modality.BuildParams
import com.badoo.ribs.samples.simplerouting.rib.simple_routing_child1_child1.SimpleRoutingChild1Child1
import com.badoo.ribs.samples.simplerouting.rib.simple_routing_child1_child1.SimpleRoutingChild1Child1Node
import com.badoo.ribs.samples.simplerouting.rib.simple_routing_child1_child1.SimpleRoutingChild1Child1ViewImpl

class SimpleRoutingChild1Child1Builder(
    private val dependency: SimpleRoutingChild1Child1.Dependency
) : SimpleBuilder<SimpleRoutingChild1Child1>() {

    override fun build(buildParams: BuildParams<Nothing?>): SimpleRoutingChild1Child1 {
        return SimpleRoutingChild1Child1Node(
                buildParams = buildParams,
                viewFactory = SimpleRoutingChild1Child1ViewImpl.Factory().invoke(deps = null)
        )
    }
}
