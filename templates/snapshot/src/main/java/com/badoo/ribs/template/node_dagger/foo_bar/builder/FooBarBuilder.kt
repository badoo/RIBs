package com.badoo.ribs.template.node_dagger.foo_bar.builder

import com.badoo.ribs.core.builder.BuildParams
import com.badoo.ribs.builder.SimpleBuilder
import com.badoo.ribs.template.node_dagger.foo_bar.FooBar

class FooBarBuilder(
    private val dependency: FooBar.Dependency
) : SimpleBuilder<FooBar>() {

    override fun build(buildParams: BuildParams<Nothing?>): FooBar =
        DaggerFooBarComponent
            .factory()
            .create(
                dependency = dependency,
                customisation = buildParams.getOrDefault(FooBar.Customisation()),
                buildParams = buildParams
            )
            .node()
}
