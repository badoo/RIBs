package com.badoo.ribs.samples.routing.simple_routing.rib.child1.routing

import com.badoo.ribs.samples.routing.simple_routing.rib.child1.Child1
import com.badoo.ribs.samples.routing.simple_routing.rib.child1_child1.Child1Child1
import com.badoo.ribs.samples.routing.simple_routing.rib.child1_child1.builder.Child1Child1Builder
import com.badoo.ribs.samples.routing.simple_routing.rib.child1_child2.Child1Child2
import com.badoo.ribs.samples.routing.simple_routing.rib.child1_child2.builder.Child1Child2Builder

internal open class Child1ChildBuilders(
    dependency: Child1.Dependency
) {
    private val subtreeDeps = SubtreeDependency(dependency)

    val child1: Child1Child1Builder = Child1Child1Builder(subtreeDeps)
    val child2: Child1Child2Builder = Child1Child2Builder(subtreeDeps)

    class SubtreeDependency(
        dependency: Child1.Dependency
    ) : Child1.Dependency by dependency,
        Child1Child1.Dependency, Child1Child2.Dependency
}
