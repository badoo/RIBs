package com.badoo.ribs.sandbox.rib.compose_parent.routing

import com.badoo.ribs.sandbox.rib.compose_leaf.ComposeLeaf
import com.badoo.ribs.sandbox.rib.compose_leaf.ComposeLeafBuilder
import com.badoo.ribs.sandbox.rib.compose_parent.ComposeParent

internal class ComposeParentChildBuilders(
    dependency: ComposeParent.Dependency
) {
    private val subtreeDeps = SubtreeDependency(dependency)

    val composeLeaf = ComposeLeafBuilder(subtreeDeps)

    class SubtreeDependency(
        dependency: ComposeParent.Dependency
    ) : ComposeParent.Dependency by dependency,
        ComposeLeaf.Dependency {
        // TODO implement subtree dependencies here
    }
}



