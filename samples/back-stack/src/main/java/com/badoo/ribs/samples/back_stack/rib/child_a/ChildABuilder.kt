package com.badoo.ribs.samples.back_stack.rib.child_a

import com.badoo.ribs.builder.SimpleBuilder
import com.badoo.ribs.core.modality.BuildParams

class ChildABuilder(
    private val dependency: ChildA.Dependency
) : SimpleBuilder<ChildA>() {

    override fun build(buildParams: BuildParams<Nothing?>): ChildA {
        return ChildANode(
            buildParams = buildParams,
            viewFactory = ChildAViewImpl.Factory().invoke(null),
            plugins = emptyList()
        )
    }

}
