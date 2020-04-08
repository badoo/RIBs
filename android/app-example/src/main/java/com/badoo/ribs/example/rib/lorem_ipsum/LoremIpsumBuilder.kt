package com.badoo.ribs.example.rib.lorem_ipsum

import android.os.Bundle
import com.badoo.ribs.core.Builder
import com.badoo.ribs.core.Node
import com.badoo.ribs.customisation.customisationsBranchFor
import com.badoo.ribs.customisation.getOrDefault

class LoremIpsumBuilder(
    dependency: LoremIpsum.Dependency
) : Builder<LoremIpsum.Dependency>() {

    override val dependency : LoremIpsum.Dependency = object : LoremIpsum.Dependency by dependency {
        override fun ribCustomisation() = dependency.customisationsBranchFor(LoremIpsum::class)
    }

    fun build(savedInstanceState: Bundle?): Node<LoremIpsumView> {
        val customisation = dependency.getOrDefault(LoremIpsum.Customisation())
        val interactor = LoremIpsumInteractor(
            savedInstanceState,
            dependency.loremIpsumOutput()
        )

        return Node(
            savedInstanceState = savedInstanceState,
            identifier = object : LoremIpsum {},
            viewFactory = customisation.viewFactory(null),
            router = null,
            interactor = interactor
        )
    }
}
