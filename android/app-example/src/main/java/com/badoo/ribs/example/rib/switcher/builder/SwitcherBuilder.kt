package com.badoo.ribs.example.rib.switcher.builder

import com.badoo.ribs.core.Builder
import com.badoo.ribs.core.Node
import com.badoo.ribs.core.customisation.customisationsBranchFor
import com.badoo.ribs.core.customisation.getOrDefault
import com.badoo.ribs.example.rib.switcher.Switcher
import com.badoo.ribs.example.rib.switcher.SwitcherView

class SwitcherBuilder(
    dependency: Switcher.Dependency
) : Builder<Switcher.Dependency>()
{
    override val dependency : Switcher.Dependency = object : Switcher.Dependency by dependency {
        override fun ribCustomisation() = dependency.customisationsBranchFor(Switcher::class)
    }

    fun build(): Node<SwitcherView> =
        DaggerSwitcherComponent
            .factory()
            .create(dependency, dependency.getOrDefault(Switcher.Customisation()))
            .node()
}
