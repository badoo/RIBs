package com.badoo.ribs.example.rib.lorem_ipsum

import com.badoo.ribs.core.Node
import com.badoo.ribs.core.builder.BuildParams
import com.badoo.ribs.core.builder.Builder
import com.badoo.ribs.customisation.customisationsBranchFor
import com.badoo.ribs.customisation.getOrDefault

class LoremIpsumBuilder(
    dependency: LoremIpsum.Dependency
) : Builder<LoremIpsum.Dependency, Node<LoremIpsumView>>(
    rib = object : LoremIpsum {}
) {

    override val dependency : LoremIpsum.Dependency = object : LoremIpsum.Dependency by dependency {
        override fun ribCustomisation() = dependency.customisationsBranchFor(LoremIpsum::class)
    }

    override fun build(buildParams: BuildParams<Nothing?>): Node<LoremIpsumView> {
        val customisation = dependency.getOrDefault(LoremIpsum.Customisation())
        val interactor = LoremIpsumInteractor(
            buildParams = buildParams,
            output = dependency.loremIpsumOutput()
        )

        return Node(
            buildParams = buildParams,
            viewFactory = customisation.viewFactory(null),
            router = null,
            interactor = interactor
        )
    }
}
