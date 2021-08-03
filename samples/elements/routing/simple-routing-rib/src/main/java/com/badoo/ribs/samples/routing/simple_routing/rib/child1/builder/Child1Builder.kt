package com.badoo.ribs.samples.routing.simple_routing.rib.child1.builder

import com.badoo.ribs.builder.SimpleBuilder
import com.badoo.ribs.core.modality.BuildParams
import com.badoo.ribs.samples.routing.simple_routing.rib.child1.Child1
import com.badoo.ribs.samples.routing.simple_routing.rib.child1.Child1Node
import com.badoo.ribs.samples.routing.simple_routing.rib.child1.Child1Presenter
import com.badoo.ribs.samples.routing.simple_routing.rib.child1.routing.Child1ChildBuilders
import com.badoo.ribs.samples.routing.simple_routing.rib.child1.routing.Child1Router

class Child1Builder(
    private val dependency: Child1.Dependency
) : SimpleBuilder<Child1>() {

    override fun build(buildParams: BuildParams<Nothing?>): Child1 {
        val customisation = buildParams.getOrDefault(Child1.Customisation())
        val presenter = Child1Presenter(title = dependency.title)
        val router = Child1Router(
            buildParams = buildParams,
            builders = Child1ChildBuilders(dependency)
        )
        return Child1Node(
            buildParams = buildParams,
            viewFactory = customisation.viewFactory(null),
            plugins = listOf(presenter, router)
        )
    }
}
