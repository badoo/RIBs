package com.badoo.ribs.samples.simplerouting.rib.simple_routing_child2.builder

import com.badoo.ribs.builder.SimpleBuilder
import com.badoo.ribs.core.modality.BuildParams
import com.badoo.ribs.samples.simplerouting.rib.simple_routing_child2.SimpleRoutingChild2
import com.badoo.ribs.samples.simplerouting.rib.simple_routing_child2.SimpleRoutingChild2Node
import com.badoo.ribs.samples.simplerouting.rib.simple_routing_child2.SimpleRoutingChild2Presenter

class SimpleRoutingChild2Builder(
    private val dependency: SimpleRoutingChild2.Dependency
) : SimpleBuilder<SimpleRoutingChild2>() {

    override fun build(buildParams: BuildParams<Nothing?>): SimpleRoutingChild2 {
        val customisation = buildParams.getOrDefault(SimpleRoutingChild2.Customisation())
        val presenter = SimpleRoutingChild2Presenter()

        return SimpleRoutingChild2Node(
            buildParams = buildParams,
            viewFactory = customisation.viewFactory(null),
            plugins = listOf(presenter)
        )
    }
}
