package com.badoo.ribs.example.rib.blocker.builder

import com.badoo.ribs.core.Builder
import com.badoo.ribs.core.Node
import com.badoo.ribs.core.customisation.customisationsBranchFor
import com.badoo.ribs.core.customisation.getOrDefault
import com.badoo.ribs.example.rib.blocker.Blocker
import com.badoo.ribs.example.rib.blocker.BlockerView

class BlockerBuilder(
    dependency: Blocker.Dependency
) : Builder<Blocker.Dependency>() {

    override val dependency : Blocker.Dependency = object : Blocker.Dependency by dependency {
        override fun ribCustomisation() = dependency.customisationsBranchFor(Blocker::class)
    }

    fun build(): Node<BlockerView> =
        DaggerBlockerComponent
            .factory()
            .create(dependency, dependency.getOrDefault(Blocker.Customisation()))
            .node()
}
