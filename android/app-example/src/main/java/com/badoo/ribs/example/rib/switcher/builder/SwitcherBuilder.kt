package com.badoo.ribs.example.rib.switcher.builder

import com.badoo.ribs.core.builder.BuildParams
import com.badoo.ribs.core.builder.Builder
import com.badoo.ribs.customisation.customisationsBranchFor
import com.badoo.ribs.customisation.getOrDefault
import com.badoo.ribs.example.rib.switcher.Switcher
import com.badoo.ribs.example.rib.switcher.SwitcherNode

class SwitcherBuilder(
    dependency: Switcher.Dependency
) : Builder<Switcher.Dependency, SwitcherNode>(object : Switcher {}) {
    override val dependency : Switcher.Dependency = object : Switcher.Dependency by dependency {
        override fun ribCustomisation() = dependency.customisationsBranchFor(Switcher::class)
    }

    override fun build(buildParams: BuildParams<Nothing?>): SwitcherNode =
        DaggerSwitcherComponent
            .factory()
            .create(
                dependency = dependency,
                customisation = dependency.getOrDefault(Switcher.Customisation()),
                buildParams = buildParams
            )
            .node()
}
