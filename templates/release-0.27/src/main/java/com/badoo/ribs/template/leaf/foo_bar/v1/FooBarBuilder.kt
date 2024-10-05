package com.badoo.ribs.template.leaf.foo_bar.v1

import com.badoo.ribs.builder.SimpleBuilder
import com.badoo.ribs.core.modality.BuildParams
import com.badoo.ribs.rx2.disposables
import com.badoo.ribs.template.leaf.foo_bar.common.FooBar
import com.badoo.ribs.template.leaf.foo_bar.common.feature.FooBarFeature

class FooBarBuilder(
    private val dependency: FooBar.Dependency
) : SimpleBuilder<FooBarRib>() {

    override fun build(buildParams: BuildParams<Nothing?>): FooBarRib {
        val customisation = buildParams.getOrDefault(FooBarRib.Customisation())
        val feature = feature()
        val interactor = interactor(buildParams, feature)

        return node(buildParams, customisation, feature, interactor)
    }

    private fun feature() =
        FooBarFeature()

    private fun interactor(
        buildParams: BuildParams<*>,
        feature: FooBarFeature
    ) = FooBarInteractor(
            buildParams = buildParams,
            feature = feature
        )

    private fun node(
        buildParams: BuildParams<Nothing?>,
        customisation: FooBarRib.Customisation,
        feature: FooBarFeature,
        interactor: FooBarInteractor
    ) = FooBarNode(
            buildParams = buildParams,
            viewFactory = customisation.viewFactory(null),
            plugins = listOf(interactor, disposables(feature))
        )
}
