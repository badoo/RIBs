package com.badoo.ribs.template.node_dagger.foo_bar.builder

import com.badoo.ribs.core.builder.BuildParams
import com.badoo.ribs.core.builder.SimpleBuilder
import com.badoo.ribs.customisation.customisationsBranchFor
import com.badoo.ribs.customisation.getOrDefault
import com.badoo.ribs.template.node_dagger.foo_bar.FooBar

class FooBarBuilder(
    dependency: FooBar.Dependency
) : SimpleBuilder<FooBar>() {

    private val dependency : FooBar.Dependency = object : FooBar.Dependency by dependency {
        override fun ribCustomisation() = dependency.customisationsBranchFor(FooBar::class)
    }

    override fun build(buildParams: BuildParams<Nothing?>): FooBar =
        DaggerFooBarComponent
            .factory()
            .create(
                dependency = dependency,
                customisation = dependency.getOrDefault(FooBar.Customisation()),
                buildParams = buildParams
            )
            .node()
}
