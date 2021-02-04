package com.badoo.ribs.samples.simplerouting.rib.simple_routing_child2.builder

import com.badoo.ribs.builder.SimpleBuilder
import com.badoo.ribs.core.modality.BuildParams
import com.badoo.ribs.samples.simplerouting.rib.simple_routing_child2.SimpleRoutingChild2
import com.badoo.ribs.samples.simplerouting.rib.simple_routing_child2.SimpleRoutingChild2Node
import com.badoo.ribs.samples.simplerouting.rib.simple_routing_child2.SimpleRoutingChild2Presenter
import com.badoo.ribs.samples.simplerouting.rib.simple_routing_child2.SimpleRoutingChild2ViewImpl

class SimpleRoutingChild2Builder(
    private val dependency: SimpleRoutingChild2.Dependency
) : SimpleBuilder<SimpleRoutingChild2>() {

    override fun build(buildParams: BuildParams<Nothing?>): SimpleRoutingChild2 {
        val presenter = SimpleRoutingChild2Presenter()

        return SimpleRoutingChild2Node(
                buildParams = buildParams,
                viewFactory = SimpleRoutingChild2ViewImpl.Factory().invoke(deps = null),
                plugins = listOf(presenter)
        )
    }
}
