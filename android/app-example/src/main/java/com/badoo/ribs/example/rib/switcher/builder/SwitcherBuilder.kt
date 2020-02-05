package com.badoo.ribs.example.rib.switcher.builder

import com.badoo.ribs.core.BuildContext
import com.badoo.ribs.core.Builder
import com.badoo.ribs.core.Rib
import com.badoo.ribs.customisation.customisationsBranchFor
import com.badoo.ribs.customisation.getOrDefault
import com.badoo.ribs.example.rib.switcher.Switcher
import com.badoo.ribs.example.rib.switcher.SwitcherNode

class SwitcherBuilder(
    dependency: Switcher.Dependency
) : Builder<Switcher.Dependency, Nothing?, SwitcherNode>()
{
    override val dependency : Switcher.Dependency = object : Switcher.Dependency by dependency {
        override fun ribCustomisation() = dependency.customisationsBranchFor(Switcher::class)
    }

    override val rib: Rib =
        object : Switcher {}

    override fun build(buildContext: BuildContext<Nothing?>): SwitcherNode =
        DaggerSwitcherComponent
            .factory()
            .create(
                dependency = dependency,
                customisation = dependency.getOrDefault(Switcher.Customisation()),
                buildContext = buildContext
            )
            .node()
}
