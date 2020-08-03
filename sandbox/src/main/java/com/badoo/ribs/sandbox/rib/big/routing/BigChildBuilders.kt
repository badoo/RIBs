package com.badoo.ribs.sandbox.rib.big.routing

import com.badoo.ribs.sandbox.rib.big.Big
import com.badoo.ribs.sandbox.rib.small.Small
import com.badoo.ribs.sandbox.rib.small.SmallBuilder

internal open class BigChildBuilders(
    dependency: Big.Dependency
) {
    private val subtreeDeps = SubtreeDependency(dependency)

    val small = SmallBuilder(subtreeDeps)

    class SubtreeDependency(
        dependency: Big.Dependency
    ) : Big.Dependency by dependency,
        Small.Dependency {

    }
}

