package com.badoo.ribs.samples.routing.simple_routing.rib.child1_child2.builder

import com.badoo.ribs.builder.SimpleBuilder
import com.badoo.ribs.core.modality.BuildParams
import com.badoo.ribs.samples.routing.simple_routing.rib.child1_child2.Child1Child2
import com.badoo.ribs.samples.routing.simple_routing.rib.child1_child2.Child1Child2Node

class Child1Child2Builder(
    private val dependency: Child1Child2.Dependency
) : SimpleBuilder<Child1Child2>() {

    override fun build(buildParams: BuildParams<Nothing?>): Child1Child2 {
        val customisation = buildParams.getOrDefault(Child1Child2.Customisation())
        return Child1Child2Node(
            buildParams = buildParams,
            viewFactory = customisation.viewFactory(null)
        )
    }
}
