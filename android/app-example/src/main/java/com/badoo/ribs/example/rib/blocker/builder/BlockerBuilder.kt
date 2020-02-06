package com.badoo.ribs.example.rib.blocker.builder

import com.badoo.ribs.core.builder.BuildParams
import com.badoo.ribs.core.builder.Builder
import com.badoo.ribs.core.Node
import com.badoo.ribs.core.Rib
import com.badoo.ribs.customisation.customisationsBranchFor
import com.badoo.ribs.customisation.getOrDefault
import com.badoo.ribs.example.rib.blocker.Blocker
import com.badoo.ribs.example.rib.blocker.BlockerView

class BlockerBuilder(
    dependency: Blocker.Dependency
) : Builder<Blocker.Dependency, Node<BlockerView>>() {

    override val dependency : Blocker.Dependency = object : Blocker.Dependency by dependency {
        override fun ribCustomisation() = dependency.customisationsBranchFor(Blocker::class)
    }

    override val rib: Rib =
        object : Blocker {}

    override fun build(buildParams: BuildParams<Nothing?>): Node<BlockerView> =
        DaggerBlockerComponent
            .factory()
            .create(
                dependency = dependency,
                customisation = dependency.getOrDefault(Blocker.Customisation()),
                buildParams = buildParams
            )
            .node()
}
