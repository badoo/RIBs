package com.badoo.ribs.samples.transitionanimations.rib.child1

import com.badoo.ribs.builder.SimpleBuilder
import com.badoo.ribs.core.modality.BuildParams

class Child1Builder(
    private val dependency: Child1.Dependency
) : SimpleBuilder<Child1>() {

    override fun build(buildParams: BuildParams<Nothing?>): Child1 {
        val customisation = buildParams.getOrDefault(Child1.Customisation())
        return Child1Node(
                buildParams = buildParams,
                viewFactory = customisation.viewFactory(null),
                plugins = emptyList()
        )
    }
}
