package com.badoo.ribs.sandbox.rib.dialog_example.routing

import com.badoo.ribs.sandbox.rib.dialog_example.DialogExample
import com.badoo.ribs.sandbox.rib.lorem_ipsum.LoremIpsum
import com.badoo.ribs.sandbox.rib.lorem_ipsum.LoremIpsumBuilder

internal open class DialogExampleChildBuilders(
    dependency: DialogExample.Dependency
) {
    private val subtreeDeps = SubtreeDependency(dependency)

    val loremIpsum = LoremIpsumBuilder(subtreeDeps)

    class SubtreeDependency(
        dependency: DialogExample.Dependency
    ) : DialogExample.Dependency by dependency,
        LoremIpsum.Dependency {

    }
}

