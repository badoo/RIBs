package com.badoo.ribs.samples.routing.back_stack.rib.child_f

import com.badoo.ribs.builder.SimpleBuilder
import com.badoo.ribs.core.modality.BuildParams

class ChildFBuilder(
    private val dependency: ChildF.Dependency
) : SimpleBuilder<ChildF>() {

    override fun build(buildParams: BuildParams<Nothing?>): ChildF {
        return ChildFNode(
            buildParams = buildParams,
            viewFactory = ChildFViewImpl.Factory().invoke(null),
            plugins = emptyList()
        )
    }

}
