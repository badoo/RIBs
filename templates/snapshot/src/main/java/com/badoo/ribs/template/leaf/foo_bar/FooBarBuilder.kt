package com.badoo.ribs.template.leaf.foo_bar

import com.badoo.ribs.builder.SimpleBuilder
import com.badoo.ribs.core.modality.BuildParams
import com.badoo.ribs.rx.disposables
import com.badoo.ribs.template.leaf.foo_bar.feature.FooBarFeature

class FooBarBuilder(
    private val dependency: FooBar.Dependency
) : SimpleBuilder<FooBar>() {

    override fun build(buildParams: BuildParams<Nothing?>): FooBar {
        val customisation = buildParams.getOrDefault(FooBar.Customisation())
        val feature = feature()
        val interactor = interactor(
            buildParams = buildParams,
            feature = feature,
        )

        val viewDependency = viewDependency()

        return node(
            buildParams = buildParams,
            customisation = customisation,
            feature = feature,
            viewDependency = viewDependency,
            interactor = interactor,
        )
    }

    private fun feature() =
        FooBarFeature()

    private fun interactor(buildParams: BuildParams<*>, feature: FooBarFeature) =
        FooBarInteractor(
            buildParams = buildParams,
            feature = feature,
        )

    private fun viewDependency(): FooBarView.ViewDependency =
        object : FooBarView.ViewDependency {

        }

    private fun node(
        buildParams: BuildParams<Nothing?>,
        customisation: FooBar.Customisation,
        feature: FooBarFeature,
        viewDependency: FooBarView.ViewDependency,
        interactor: FooBarInteractor,
    ) =
        FooBarNode(
            buildParams = buildParams,
            viewFactory = customisation.viewFactory(viewDependency),
            plugins = listOf(
                interactor,
                disposables(feature),
            )
        )
}
