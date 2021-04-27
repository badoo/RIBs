package com.badoo.ribs.samples.transitionanimations.rib.child3

import com.badoo.ribs.builder.SimpleBuilder
import com.badoo.ribs.core.modality.BuildParams

class Child3Builder : SimpleBuilder<Child3>() {

    override fun build(buildParams: BuildParams<Nothing?>): Child3 =
        Child3Node(
            buildParams = buildParams,
            viewFactory = Child3ViewImpl.Factory().invoke(deps = null),
            plugins = emptyList()
        )
}
