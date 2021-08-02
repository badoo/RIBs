package com.badoo.ribs.samples.routing.simple_routing.rib.child2.builder

import com.badoo.ribs.builder.SimpleBuilder
import com.badoo.ribs.core.modality.BuildParams
import com.badoo.ribs.samples.routing.simple_routing.rib.child2.Child2
import com.badoo.ribs.samples.routing.simple_routing.rib.child2.Child2Node
import com.badoo.ribs.samples.routing.simple_routing.rib.child2.Child2Presenter

class Child2Builder(
    private val dependency: Child2.Dependency
) : SimpleBuilder<Child2>() {

    override fun build(buildParams: BuildParams<Nothing?>): Child2 {
        val customisation = buildParams.getOrDefault(Child2.Customisation())
        val presenter = Child2Presenter()

        return Child2Node(
            buildParams = buildParams,
            viewFactory = customisation.viewFactory(null),
            plugins = listOf(presenter)
        )
    }
}
