package com.badoo.ribs.example.rib.switcher.builder

import android.os.Bundle
import com.badoo.ribs.core.Builder
import com.badoo.ribs.core.Node
import com.badoo.ribs.customisation.customisationsBranchFor
import com.badoo.ribs.customisation.getOrDefault
import com.badoo.ribs.example.rib.switcher.Switcher
import com.badoo.ribs.example.rib.switcher.SwitcherView

class SwitcherBuilder(
    dependency: Switcher.Dependency
) : Builder<Switcher.Dependency>()
{
    override val dependency : Switcher.Dependency = object : Switcher.Dependency by dependency {
        override fun ribCustomisation() = dependency.customisationsBranchFor(Switcher::class)
    }

    fun build(savedInstanceState: Bundle?): Node<SwitcherView> =
        DaggerSwitcherComponent
            .factory()
            .create(
                dependency = dependency,
                customisation = dependency.getOrDefault(Switcher.Customisation()),
                savedInstanceState =savedInstanceState
            )
            .node()
}
