@file:SuppressWarnings("LongParameterList")

package com.badoo.ribs.template.node.foo_bar

import com.badoo.ribs.core.builder.BuildParams
import com.badoo.ribs.core.builder.SimpleBuilder
import com.badoo.ribs.template.node.foo_bar.feature.FooBarFeature

class FooBarBuilder(
    private val dependency: FooBar.Dependency
) : SimpleBuilder<FooBar>() {

    override fun build(buildParams: BuildParams<Nothing?>): FooBar {
        val customisation = buildParams.getOrDefault(FooBar.Customisation())
        val router = router(buildParams, customisation)
        val feature = feature()
        val interactor = interactor(dependency, buildParams, router, feature)

        return node(buildParams, customisation, router, interactor, feature)
    }

    private fun feature() =
        FooBarFeature()

    private fun router(
        buildParams: BuildParams<*>,
        customisation: FooBar.Customisation
    ) =
        FooBarRouter(
            buildParams = buildParams,
            transitionHandler = customisation.transitionHandler
        )

    private fun interactor(
        dependency: FooBar.Dependency,
        buildParams: BuildParams<*>,
        router: FooBarRouter,
        feature: FooBarFeature
    ) = FooBarInteractor(
            buildParams = buildParams,
            router = router,
            input = dependency.fooBarInput(),
            output = dependency.fooBarOutput(),
            feature = feature
        )

    private fun node(
        buildParams: BuildParams<*>,
        customisation: FooBar.Customisation,
        router: FooBarRouter,
        interactor: FooBarInteractor,
        feature: FooBarFeature
    ) = FooBarNode(
        buildParams = buildParams,
        viewFactory = customisation.viewFactory(null),
        router = router,
        interactor = interactor,
        feature = feature
    )
}
