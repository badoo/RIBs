package com.badoo.ribs.samples.routing.back_stack.rib.child_c

import com.badoo.ribs.builder.SimpleBuilder
import com.badoo.ribs.core.modality.BuildParams

class ChildCBuilder(
    private val dependency: ChildC.Dependency
) : SimpleBuilder<ChildC>() {

    override fun build(buildParams: BuildParams<Nothing?>): ChildC {
        return ChildCNode(
            buildParams = buildParams,
            viewFactory = ChildCViewImpl.Factory().invoke(null),
            plugins = emptyList()
        )
    }

}
