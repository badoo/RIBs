package com.badoo.ribs.samples.routing.simple_routing.rib.simple_routing_parent.builder

import com.badoo.ribs.builder.SimpleBuilder
import com.badoo.ribs.core.modality.BuildParams
import com.badoo.ribs.samples.routing.simple_routing.rib.simple_routing_parent.SimpleRoutingParent
import com.badoo.ribs.samples.routing.simple_routing.rib.simple_routing_parent.SimpleRoutingParentNode
import com.badoo.ribs.samples.routing.simple_routing.rib.simple_routing_parent.routing.SimpleRoutingParentChildBuilders
import com.badoo.ribs.samples.routing.simple_routing.rib.simple_routing_parent.routing.SimpleRoutingParentRouter

class SimpleRoutingParentBuilder(
    private val dependency: SimpleRoutingParent.Dependency
) : SimpleBuilder<SimpleRoutingParent>() {

    override fun build(buildParams: BuildParams<Nothing?>): SimpleRoutingParent {
        val customisation = buildParams.getOrDefault(SimpleRoutingParent.Customisation())
        val router = SimpleRoutingParentRouter(
            buildParams = buildParams,
            builders = SimpleRoutingParentChildBuilders(dependency)
        )
        return SimpleRoutingParentNode(
            buildParams = buildParams,
            viewFactory = customisation.viewFactory(null),
            plugins = listOf(router)
        )
    }
}
