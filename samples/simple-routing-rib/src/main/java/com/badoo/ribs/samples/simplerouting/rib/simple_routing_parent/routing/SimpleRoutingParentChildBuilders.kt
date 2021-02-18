package com.badoo.ribs.samples.simplerouting.rib.simple_routing_parent.routing

import com.badoo.ribs.samples.simplerouting.rib.simple_routing_child1.SimpleRoutingChild1
import com.badoo.ribs.samples.simplerouting.rib.simple_routing_child1.builder.SimpleRoutingChild1Builder
import com.badoo.ribs.samples.simplerouting.rib.simple_routing_child2.SimpleRoutingChild2
import com.badoo.ribs.samples.simplerouting.rib.simple_routing_child2.builder.SimpleRoutingChild2Builder
import com.badoo.ribs.samples.simplerouting.rib.simple_routing_parent.SimpleRoutingParent

internal open class SimpleRoutingParentChildBuilders(
    dependency: SimpleRoutingParent.Dependency
) {
    private val subtreeDeps = SubtreeDependency(dependency)

    val child1: SimpleRoutingChild1Builder = SimpleRoutingChild1Builder(subtreeDeps)
    val child2: SimpleRoutingChild2Builder = SimpleRoutingChild2Builder(subtreeDeps)

    class SubtreeDependency(
        dependency: SimpleRoutingParent.Dependency
    ) : SimpleRoutingParent.Dependency by dependency,
        SimpleRoutingChild1.Dependency, SimpleRoutingChild2.Dependency {
        override val title: String = "Child 1 title"
    }
}
