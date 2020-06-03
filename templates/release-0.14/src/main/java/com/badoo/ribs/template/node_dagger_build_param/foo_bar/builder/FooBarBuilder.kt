package com.badoo.ribs.template.node_dagger_build_param.foo_bar.builder

import com.badoo.ribs.core.builder.BuildParams
import com.badoo.ribs.core.builder.Builder
import com.badoo.ribs.customisation.customisationsBranchFor
import com.badoo.ribs.customisation.getOrDefault
import com.badoo.ribs.template.node_dagger_build_param.foo_bar.FooBar
import com.badoo.ribs.template.node_dagger_build_param.foo_bar.FooBarNode
import com.badoo.ribs.template.node_dagger_build_param.foo_bar.builder.FooBarBuilder.Params

class FooBarBuilder(
    dependency: FooBar.Dependency
) : Builder<Params, FooBarNode>() {

    data class Params(
        val someField: Int
    )

    private val dependency : FooBar.Dependency = object : FooBar.Dependency by dependency {
        override fun ribCustomisation() = dependency.customisationsBranchFor(FooBar::class)
    }

    override fun build(buildParams: BuildParams<Params>): FooBarNode =
        DaggerFooBarComponent
            .factory()
            .create(
                dependency = dependency,
                customisation = dependency.getOrDefault(FooBar.Customisation()),
                buildParams = buildParams
            )
            .node()
}
