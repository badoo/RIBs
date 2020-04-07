package com.badoo.ribs.example.rib.blocker

import com.badoo.ribs.core.builder.BuildParams
import com.badoo.ribs.core.builder.SimpleBuilder
import com.badoo.ribs.core.Node
import com.badoo.ribs.customisation.customisationsBranchFor
import com.badoo.ribs.customisation.getOrDefault

class BlockerBuilder(
    dependency: Blocker.Dependency
) : SimpleBuilder<Node<BlockerView>>(
    rib = object : Blocker {}
) {

    private val dependency : Blocker.Dependency = object : Blocker.Dependency by dependency {
        override fun ribCustomisation() = dependency.customisationsBranchFor(Blocker::class)
    }

    override fun build(buildParams: BuildParams<Nothing?>): Node<BlockerView> {
        val customisation = dependency.getOrDefault(Blocker.Customisation())
        val interactor = BlockerInteractor(
            buildParams = buildParams,
            output = dependency.blockerOutput()
        )

        return Node(
            buildParams = buildParams,
            viewFactory = customisation.viewFactory(null),
            router = null,
            interactor = interactor
        )
    }
}
