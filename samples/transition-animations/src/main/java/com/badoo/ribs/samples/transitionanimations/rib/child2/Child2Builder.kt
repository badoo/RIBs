package com.badoo.ribs.samples.transitionanimations.rib.child2

import com.badoo.ribs.builder.SimpleBuilder
import com.badoo.ribs.core.modality.BuildParams

class Child2Builder(
    private val dependency: Child2.Dependency
) : SimpleBuilder<Child2>() {

    override fun build(buildParams: BuildParams<Nothing?>): Child2 {
        val customisation = buildParams.getOrDefault(Child2.Customisation())

        return Child2Node(
                buildParams = buildParams,
                viewFactory = customisation.viewFactory(null),
                plugins = emptyList()
        )
    }
}
