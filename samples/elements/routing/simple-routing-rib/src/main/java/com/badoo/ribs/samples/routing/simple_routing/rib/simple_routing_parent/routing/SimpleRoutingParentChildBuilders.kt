package com.badoo.ribs.samples.routing.simple_routing.rib.simple_routing_parent.routing

import com.badoo.ribs.samples.routing.simple_routing.rib.child1.Child1
import com.badoo.ribs.samples.routing.simple_routing.rib.child1.builder.Child1Builder
import com.badoo.ribs.samples.routing.simple_routing.rib.child2.Child2
import com.badoo.ribs.samples.routing.simple_routing.rib.child2.builder.Child2Builder
import com.badoo.ribs.samples.routing.simple_routing.rib.simple_routing_parent.SimpleRoutingParent

internal open class SimpleRoutingParentChildBuilders(
    dependency: SimpleRoutingParent.Dependency
) {
    private val subtreeDeps = SubtreeDependency(dependency)

    val child1: Child1Builder = Child1Builder(subtreeDeps)
    val child2: Child2Builder = Child2Builder(subtreeDeps)

    class SubtreeDependency(
        dependency: SimpleRoutingParent.Dependency
    ) : SimpleRoutingParent.Dependency by dependency,
        Child1.Dependency, Child2.Dependency {
        override val title: String = "Child 1 title"
    }
}
