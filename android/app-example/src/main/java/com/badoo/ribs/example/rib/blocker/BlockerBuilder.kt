package com.badoo.ribs.example.rib.blocker

import android.os.Bundle
import com.badoo.ribs.core.Builder
import com.badoo.ribs.core.Node
import com.badoo.ribs.customisation.customisationsBranchFor
import com.badoo.ribs.customisation.getOrDefault

class BlockerBuilder(
    dependency: Blocker.Dependency
) : Builder<Blocker.Dependency>() {

    override val dependency : Blocker.Dependency = object : Blocker.Dependency by dependency {
        override fun ribCustomisation() = dependency.customisationsBranchFor(Blocker::class)
    }

    fun build(savedInstanceState: Bundle? ): Node<BlockerView> {
        val customisation = dependency.getOrDefault(Blocker.Customisation())
        val interactor = BlockerInteractor(
            savedInstanceState,
            dependency.blockerOutput()
        )

        return Node(
            savedInstanceState = savedInstanceState,
            identifier = object : Blocker {},
            viewFactory = customisation.viewFactory(null),
            router = null,
            interactor = interactor
        )
    }
}
