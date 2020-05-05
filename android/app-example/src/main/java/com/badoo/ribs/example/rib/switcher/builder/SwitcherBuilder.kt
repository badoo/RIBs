package com.badoo.ribs.example.rib.switcher.builder

import com.badoo.ribs.core.builder.BuildParams
import com.badoo.ribs.core.builder.SimpleBuilder
import com.badoo.ribs.example.rib.switcher.Switcher

class SwitcherBuilder(
    private val dependency: Switcher.Dependency
) : SimpleBuilder<Switcher>() {

    override fun build(buildParams: BuildParams<Nothing?>): Switcher =
        DaggerSwitcherComponent
            .factory()
            .create(
                dependency = dependency,
                customisation = buildParams.getOrDefault(Switcher.Customisation()),
                buildParams = buildParams
            )
            .node()
}
