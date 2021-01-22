package com.badoo.ribs.samples.simplerouting.simple_routing_child1.routing

import com.badoo.ribs.samples.simplerouting.simple_routing_child1.SimpleRoutingChild1
import com.badoo.ribs.samples.simplerouting.simple_routing_child1_child1.SimpleRoutingChild1Child1
import com.badoo.ribs.samples.simplerouting.simple_routing_child1_child1.builder.SimpleRoutingChild1Child1Builder
import com.badoo.ribs.samples.simplerouting.simple_routing_child1_child2.SimpleRoutingChild1Child2
import com.badoo.ribs.samples.simplerouting.simple_routing_child1_child2.builder.SimpleRoutingChild1Child2Builder
import com.badoo.ribs.samples.simplerouting.simple_routing_parent.SimpleRoutingParent

internal open class SimpleRoutingChild1ChildBuilders(
    dependency: SimpleRoutingChild1.Dependency
) {
    private val subtreeDeps = SubtreeDependency(dependency)

    val child1 = SimpleRoutingChild1Child1Builder(subtreeDeps)
    val child2 = SimpleRoutingChild1Child2Builder(subtreeDeps)

    class SubtreeDependency(
        dependency: SimpleRoutingChild1.Dependency
    ) : SimpleRoutingChild1.Dependency by dependency,
        SimpleRoutingChild1Child1.Dependency, SimpleRoutingChild1Child2.Dependency
}
