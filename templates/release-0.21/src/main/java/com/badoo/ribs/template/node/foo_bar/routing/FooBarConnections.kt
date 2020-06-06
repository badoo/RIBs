package com.badoo.ribs.template.node.foo_bar.routing

import com.badoo.ribs.template.node.foo_bar.FooBar

internal class FooBarConnections(
    dependency: FooBar.Dependency
) {
    private val subtreeDeps = SubtreeDependency(dependency)

    // TODO add public fields for all children
    // val child1 = Child1Builder(subtreeDeps)

    class SubtreeDependency(
        dependency: FooBar.Dependency
    ) : FooBar.Dependency by dependency
        // TODO enumerate dependencies of children this Rib can host
        // , Child1.Dependency
    {
        // TODO implement subtree dependencies here
    }
}



