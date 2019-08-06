package com.badoo.ribs.example.rib.blocker.builder

import android.os.Bundle
import com.badoo.ribs.core.Builder
import com.badoo.ribs.core.Node
import com.badoo.ribs.customisation.customisationsBranchFor
import com.badoo.ribs.customisation.getOrDefault
import com.badoo.ribs.example.rib.blocker.Blocker
import com.badoo.ribs.example.rib.blocker.BlockerView

class BlockerBuilder(
    dependency: Blocker.Dependency
) : Builder<Blocker.Dependency>() {

    override val dependency : Blocker.Dependency = object : Blocker.Dependency by dependency {
        override fun ribCustomisation() = dependency.customisationsBranchFor(Blocker::class)
    }

    fun build(savedInstanceState: Bundle? ): Node<BlockerView> =
        DaggerBlockerComponent
            .factory()
            .create(
                dependency = dependency,
                customisation = dependency.getOrDefault(Blocker.Customisation()),
                savedInstanceState = savedInstanceState
            )
            .node()
}
