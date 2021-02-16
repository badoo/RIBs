package com.badoo.ribs.template.leaf_view_only.foo_bar

import com.badoo.ribs.builder.SimpleBuilder
import com.badoo.ribs.core.modality.BuildParams


class FooBarBuilder(
    private val dependency: FooBar.Dependency
) : SimpleBuilder<FooBar>() {

    override fun build(buildParams: BuildParams<Nothing?>): FooBar {
        val customisation = buildParams.getOrDefault(FooBar.Customisation())

        return node(buildParams, customisation)
    }

    private fun node(
        buildParams: BuildParams<Nothing?>,
        customisation: FooBar.Customisation
    ) =
        FooBarNode(
            buildParams = buildParams,
            viewFactory = customisation.viewFactory(null),
            plugins = emptyList()
        )
}
