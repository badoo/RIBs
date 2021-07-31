package com.badoo.ribs.samples.routing.simple_routing.rib.child1_child1.builder

import com.badoo.ribs.builder.SimpleBuilder
import com.badoo.ribs.core.modality.BuildParams
import com.badoo.ribs.samples.routing.simple_routing.rib.child1_child1.Child1Child1
import com.badoo.ribs.samples.routing.simple_routing.rib.child1_child1.Child1Child1Node

class Child1Child1Builder(
    private val dependency: Child1Child1.Dependency
) : SimpleBuilder<Child1Child1>() {

    override fun build(buildParams: BuildParams<Nothing?>): Child1Child1 {
        val customisation = buildParams.getOrDefault(Child1Child1.Customisation())
        return Child1Child1Node(
            buildParams = buildParams,
            viewFactory = customisation.viewFactory(null)
        )
    }
}
