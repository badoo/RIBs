package com.badoo.ribs.template.leaf.foo_bar.builder

import com.badoo.ribs.builder.Builder
import com.badoo.ribs.core.modality.BuildParams
import com.badoo.ribs.rx.disposables
import com.badoo.ribs.template.leaf.foo_bar.FooBar
import com.badoo.ribs.template.leaf.foo_bar.FooBarInteractor
import com.badoo.ribs.template.leaf.foo_bar.FooBarNode
import com.badoo.ribs.template.leaf.foo_bar.FooBarView
import com.badoo.ribs.template.leaf.foo_bar.builder.FooBarBuilder.Params
import com.badoo.ribs.template.leaf.foo_bar.feature.FooBarFeature

class FooBarBuilder(
    private val dependency: FooBar.Dependency
) : Builder<Params, FooBar>() {

    class Params

    override fun build(buildParams: BuildParams<Params>): FooBar {
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
        buildParams: BuildParams<Params>,
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
