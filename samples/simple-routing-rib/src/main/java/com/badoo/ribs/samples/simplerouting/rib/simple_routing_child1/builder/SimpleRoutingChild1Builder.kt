package com.badoo.ribs.samples.simplerouting.rib.simple_routing_child1.builder

import com.badoo.ribs.builder.SimpleBuilder
import com.badoo.ribs.core.modality.BuildParams
import com.badoo.ribs.samples.simplerouting.rib.simple_routing_child1.SimpleRoutingChild1
import com.badoo.ribs.samples.simplerouting.rib.simple_routing_child1.SimpleRoutingChild1Node
import com.badoo.ribs.samples.simplerouting.rib.simple_routing_child1.SimpleRoutingChild1Presenter
import com.badoo.ribs.samples.simplerouting.rib.simple_routing_child1.routing.SimpleRoutingChild1ChildBuilders
import com.badoo.ribs.samples.simplerouting.rib.simple_routing_child1.routing.SimpleRoutingChild1Router

class SimpleRoutingChild1Builder(
    private val dependency: SimpleRoutingChild1.Dependency
) : SimpleBuilder<SimpleRoutingChild1>() {

    override fun build(buildParams: BuildParams<Nothing?>): SimpleRoutingChild1 {
        val customisation = buildParams.getOrDefault(SimpleRoutingChild1.Customisation())
        val presenter = SimpleRoutingChild1Presenter(title = dependency.title)
        val router = SimpleRoutingChild1Router(
            buildParams = buildParams,
            builders = SimpleRoutingChild1ChildBuilders(dependency)
        )
        return SimpleRoutingChild1Node(
            buildParams = buildParams,
            viewFactory = customisation.viewFactory(null),
            plugins = listOf(presenter, router)
        )
    }
}
