package com.badoo.ribs.samples.routing.back_stack.rib.child_e

import com.badoo.ribs.builder.SimpleBuilder
import com.badoo.ribs.core.modality.BuildParams

class ChildEBuilder(
    private val dependency: ChildE.Dependency
) : SimpleBuilder<ChildE>() {

    override fun build(buildParams: BuildParams<Nothing?>): ChildE {
        return ChildENode(
            buildParams = buildParams,
            viewFactory = ChildEViewImpl.Factory().invoke(null),
            plugins = emptyList()
        )
    }

}
