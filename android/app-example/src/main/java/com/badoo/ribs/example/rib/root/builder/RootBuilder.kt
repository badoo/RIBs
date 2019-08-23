package com.badoo.ribs.example.rib.root.builder

import android.os.Bundle
import com.badoo.ribs.core.Builder
import com.badoo.ribs.customisation.customisationsBranchFor
import com.badoo.ribs.customisation.getOrDefault
import com.badoo.ribs.example.rib.root.Root
import com.badoo.ribs.example.rib.root.RootNode

class RootBuilder(
    dependency: Root.Dependency
) : Builder<Root.Dependency>() {

    override val dependency : Root.Dependency = object : Root.Dependency by dependency {
        override fun ribCustomisation() = dependency.customisationsBranchFor(Root::class)
    }

    fun build(savedInstanceState: Bundle?): RootNode =
        DaggerRootComponent
            .factory()
            .create(
                dependency = dependency,
                customisation = dependency.getOrDefault(Root.Customisation()),
                savedInstanceState = savedInstanceState
            )
            .node()
}
