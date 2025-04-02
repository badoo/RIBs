package com.badoo.ribs.sandbox.rib.hello_world.routing

import com.badoo.ribs.sandbox.rib.hello_world.HelloWorld
import com.badoo.ribs.sandbox.rib.small.Small
import com.badoo.ribs.sandbox.rib.small.SmallBuilder

internal open class HelloWorldChildBuilders(
    dependency: HelloWorld.Dependency
) {
    private val subtreeDeps = SubtreeDependency(dependency)

    val small = SmallBuilder(subtreeDeps)

    class SubtreeDependency(
        dependency: HelloWorld.Dependency
    ) : HelloWorld.Dependency by dependency,
        Small.Dependency
}

