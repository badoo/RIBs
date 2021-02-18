package com.badoo.ribs.samples.comms_nodes_1.rib.child2

import com.badoo.ribs.builder.SimpleBuilder
import com.badoo.ribs.core.modality.BuildParams

class Child2Builder(
    private val dependency: Child2.Dependency
) : SimpleBuilder<Child2>() {

    override fun build(buildParams: BuildParams<Nothing?>): Child2 {
        return Child2Node(
            buildParams = buildParams,
            viewFactory = Child2ViewImpl.Factory().invoke(deps = null)
        )
    }
}
