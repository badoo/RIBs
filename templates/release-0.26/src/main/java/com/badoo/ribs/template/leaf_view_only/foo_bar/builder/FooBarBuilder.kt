package com.badoo.ribs.template.leaf_view_only.foo_bar.builder

import com.badoo.ribs.builder.Builder
import com.badoo.ribs.core.modality.BuildParams
import com.badoo.ribs.template.leaf_view_only.foo_bar.FooBar
import com.badoo.ribs.template.leaf_view_only.foo_bar.FooBarNode
import com.badoo.ribs.template.leaf_view_only.foo_bar.FooBarView
import com.badoo.ribs.template.leaf_view_only.foo_bar.builder.FooBarBuilder.Params

class FooBarBuilder(
    private val dependency: FooBar.Dependency
) : Builder<Params, FooBar>() {

    class Params

    override fun build(buildParams: BuildParams<Params>): FooBar {
        val customisation = buildParams.getOrDefault(FooBar.Customisation())
        val viewDependency = viewDependency()

        return node(
            buildParams = buildParams,
            viewDependency = viewDependency,
            customisation = customisation,
        )
    }

    private fun viewDependency(): FooBarView.ViewDependency =
        object : FooBarView.ViewDependency {}

    private fun node(
        buildParams: BuildParams<Params>,
        viewDependency: FooBarView.ViewDependency,
        customisation: FooBar.Customisation,
    ) = FooBarNode(
        buildParams = buildParams,
        viewFactory = customisation.viewFactory(viewDependency),
        plugins = emptyList(),
    )
}
