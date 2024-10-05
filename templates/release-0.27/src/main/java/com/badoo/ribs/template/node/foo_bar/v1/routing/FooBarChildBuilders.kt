package com.badoo.ribs.template.node.foo_bar.v1.routing

import com.badoo.ribs.template.node.foo_bar.common.FooBar

internal class FooBarChildBuilders(
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



