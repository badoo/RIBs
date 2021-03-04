package com.badoo.ribs.samples.transitionanimations.rib.child1

import com.badoo.ribs.builder.SimpleBuilder
import com.badoo.ribs.core.modality.BuildParams

class Child1Builder : SimpleBuilder<Child1>() {

    override fun build(buildParams: BuildParams<Nothing?>): Child1 =
        Child1Node(
            buildParams = buildParams,
            viewFactory = Child1ViewImpl.Factory().invoke(deps = null),
            plugins = emptyList()
        )
}