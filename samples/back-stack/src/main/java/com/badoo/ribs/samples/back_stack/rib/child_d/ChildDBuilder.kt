package com.badoo.ribs.samples.back_stack.rib.child_d

import com.badoo.ribs.builder.SimpleBuilder
import com.badoo.ribs.core.modality.BuildParams

class ChildDBuilder(
    private val dependency: ChildD.Dependency
) : SimpleBuilder<ChildD>() {

    override fun build(buildParams: BuildParams<Nothing?>): ChildD {
        return ChildDNode(
            buildParams = buildParams,
            viewFactory = ChildDViewImpl.Factory().invoke(null),
            plugins = emptyList()
        )
    }

}
