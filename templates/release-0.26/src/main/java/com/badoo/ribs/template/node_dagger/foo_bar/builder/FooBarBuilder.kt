package com.badoo.ribs.template.node_dagger.foo_bar.builder

import com.badoo.ribs.builder.Builder
import com.badoo.ribs.core.modality.BuildParams
import com.badoo.ribs.template.node_dagger.foo_bar.FooBar
import com.badoo.ribs.template.node_dagger.foo_bar.builder.FooBarBuilder.Params

class FooBarBuilder(
    private val dependency: FooBar.Dependency
) : Builder<Params, FooBar>() {

    class Params

    override fun build(buildParams: BuildParams<Params>): FooBar =
        DaggerFooBarComponent
            .factory()
            .create(
                dependency = dependency,
                customisation = buildParams.getOrDefault(FooBar.Customisation()),
                buildParams = buildParams
            )
            .node()
}
