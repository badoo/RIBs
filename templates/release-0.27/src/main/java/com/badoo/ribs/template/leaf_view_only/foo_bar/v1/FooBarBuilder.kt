package com.badoo.ribs.template.leaf_view_only.foo_bar.v1

import com.badoo.ribs.builder.SimpleBuilder
import com.badoo.ribs.core.modality.BuildParams
import com.badoo.ribs.template.leaf_view_only.foo_bar.common.FooBar


class FooBarBuilder(
    private val dependency: FooBar.Dependency
) : SimpleBuilder<FooBarRib>() {

    override fun build(buildParams: BuildParams<Nothing?>): FooBarRib {
        val customisation = buildParams.getOrDefault(FooBarRib.Customisation())

        return node(buildParams, customisation)
    }

    private fun node(
        buildParams: BuildParams<Nothing?>,
        customisation: FooBarRib.Customisation
    ) = FooBarNode(
            buildParams = buildParams,
            viewFactory = customisation.viewFactory(null),
            plugins = emptyList()
        )
}
