package com.badoo.ribs.example.rib.lorem_ipsum.builder

import com.badoo.ribs.core.Builder
import com.badoo.ribs.core.Node
import com.badoo.ribs.core.BuildParams
import com.badoo.ribs.core.Rib
import com.badoo.ribs.customisation.customisationsBranchFor
import com.badoo.ribs.customisation.getOrDefault
import com.badoo.ribs.example.rib.lorem_ipsum.LoremIpsum
import com.badoo.ribs.example.rib.lorem_ipsum.LoremIpsumView

class LoremIpsumBuilder(
    dependency: LoremIpsum.Dependency
) : Builder<LoremIpsum.Dependency, Nothing?, Node<LoremIpsumView>>() {

    override val dependency : LoremIpsum.Dependency = object : LoremIpsum.Dependency by dependency {
        override fun ribCustomisation() = dependency.customisationsBranchFor(LoremIpsum::class)
    }

    override val rib: Rib =
        object : LoremIpsum {}

    override fun build(buildParams: BuildParams<Nothing?>): Node<LoremIpsumView> =
        DaggerLoremIpsumComponent
            .factory()
            .create(
                dependency = dependency,
                customisation = dependency.getOrDefault(LoremIpsum.Customisation()),
                buildParams = buildParams
            )
            .node()
}
