package com.badoo.ribs.samples.menu_example.rib.child3

import com.badoo.ribs.builder.SimpleBuilder
import com.badoo.ribs.core.modality.BuildParams

class Child3Builder(
    private val dependency: Child3.Dependency
) : SimpleBuilder<Child3>() {

    override fun build(buildParams: BuildParams<Nothing?>): Child3 {
        return Child3Node(
            buildParams = buildParams,
            viewFactory = Child3ViewImpl.Factory().invoke(deps = null)
        )
    }
}
