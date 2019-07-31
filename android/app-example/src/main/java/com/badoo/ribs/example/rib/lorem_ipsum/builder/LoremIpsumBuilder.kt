package com.badoo.ribs.example.rib.lorem_ipsum.builder

import android.os.Bundle
import com.badoo.ribs.core.Builder
import com.badoo.ribs.core.Node
import com.badoo.ribs.customisation.customisationsBranchFor
import com.badoo.ribs.customisation.getOrDefault
import com.badoo.ribs.example.rib.lorem_ipsum.LoremIpsum
import com.badoo.ribs.example.rib.lorem_ipsum.LoremIpsumView

class LoremIpsumBuilder(
    dependency: LoremIpsum.Dependency
) : Builder<LoremIpsum.Dependency>() {

    override val dependency : LoremIpsum.Dependency = object : LoremIpsum.Dependency by dependency {
        override fun ribCustomisation() = dependency.customisationsBranchFor(LoremIpsum::class)
    }

    fun build(savedInstanceState: Bundle?): Node<LoremIpsumView> =
        DaggerLoremIpsumComponent
            .factory()
            .create(
                dependency = dependency,
                customisation = dependency.getOrDefault(LoremIpsum.Customisation()),
                savedInstanceState = savedInstanceState
            )
            .node()
}
