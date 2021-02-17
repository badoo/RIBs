package com.badoo.ribs.samples.comms_nodes_1.rib.child1

import com.badoo.ribs.builder.SimpleBuilder
import com.badoo.ribs.core.modality.BuildParams

class Child1Builder(
    private val dependency: Child1.Dependency
) : SimpleBuilder<Child1>() {

    override fun build(buildParams: BuildParams<Nothing?>): Child1 {
        return Child1Node(
            buildParams = buildParams,
            viewFactory = Child1ViewImpl.Factory().invoke(deps = null)
        )
    }
}
