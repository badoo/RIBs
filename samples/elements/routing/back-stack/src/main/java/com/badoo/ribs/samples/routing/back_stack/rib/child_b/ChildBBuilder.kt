package com.badoo.ribs.samples.routing.back_stack.rib.child_b

import com.badoo.ribs.builder.SimpleBuilder
import com.badoo.ribs.core.modality.BuildParams

class ChildBBuilder(
    private val dependency: ChildB.Dependency
) : SimpleBuilder<ChildB>() {

    override fun build(buildParams: BuildParams<Nothing?>): ChildB {
        return ChildBNode(
            buildParams = buildParams,
            viewFactory = ChildBViewImpl.Factory().invoke(null),
            plugins = emptyList()
        )
    }

}
