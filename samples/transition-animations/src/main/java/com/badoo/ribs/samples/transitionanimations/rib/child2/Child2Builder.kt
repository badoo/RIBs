package com.badoo.ribs.samples.transitionanimations.rib.child2

import com.badoo.ribs.builder.SimpleBuilder
import com.badoo.ribs.core.modality.BuildParams

class Child2Builder : SimpleBuilder<Child2>() {

    override fun build(buildParams: BuildParams<Nothing?>): Child2 =
        Child2Node(
            buildParams = buildParams,
            viewFactory = Child2ViewImpl.Factory().invoke(deps = null),
            plugins = emptyList()
        )
}
