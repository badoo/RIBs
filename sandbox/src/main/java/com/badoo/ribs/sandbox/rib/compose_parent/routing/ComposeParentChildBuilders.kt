package com.badoo.ribs.sandbox.rib.compose_parent.routing

import com.badoo.ribs.sandbox.rib.compose_parent.ComposeParent

internal class ComposeParentChildBuilders(
    dependency: ComposeParent.Dependency
) {
    private val subtreeDeps = SubtreeDependency(dependency)

    // TODO add public fields for all children
    // val child1 = Child1Builder(subtreeDeps)

    class SubtreeDependency(
        dependency: ComposeParent.Dependency
    ) : ComposeParent.Dependency by dependency
        // TODO enumerate dependencies of children this Rib can host
        // , Child1.Dependency
    {
        // TODO implement subtree dependencies here
    }
}



