package com.badoo.ribs.samples.transitionanimations.rib.parent.routing

import com.badoo.ribs.samples.transitionanimations.rib.child1.Child1
import com.badoo.ribs.samples.transitionanimations.rib.child1.Child1Builder
import com.badoo.ribs.samples.transitionanimations.rib.child2.Child2
import com.badoo.ribs.samples.transitionanimations.rib.child2.Child2Builder
import com.badoo.ribs.samples.transitionanimations.rib.parent.Parent

internal open class ParentChildBuilders(
        dependency: Parent.Dependency
) {

    private val subtreeDeps = SubtreeDependency(dependency)

    val child1Builder: Child1Builder = Child1Builder(subtreeDeps)
    val child2Builder: Child2Builder = Child2Builder(subtreeDeps)

    class SubtreeDependency(
            dependency: Parent.Dependency
    ) : Parent.Dependency by dependency,
            Child1.Dependency,
            Child2.Dependency
}