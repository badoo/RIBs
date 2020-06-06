@file:SuppressWarnings("LongParameterList")

package com.badoo.ribs.template.node.foo_bar

import com.badoo.ribs.core.builder.BuildParams
import com.badoo.ribs.core.builder.SimpleBuilder
import com.badoo.ribs.template.node.foo_bar.feature.FooBarFeature
import com.badoo.ribs.template.node.foo_bar.routing.FooBarConnections
import com.badoo.ribs.template.node.foo_bar.routing.FooBarRouter

class FooBarBuilder(
    private val dependency: FooBar.Dependency
) : SimpleBuilder<FooBar>() {

    override fun build(buildParams: BuildParams<Nothing?>): FooBar {
        val connections = FooBarConnections(dependency)
        val customisation = buildParams.getOrDefault(FooBar.Customisation())
        val router = router(buildParams, connections, customisation)
        val feature = feature()
        val interactor = interactor(buildParams, router, feature)

        return node(buildParams, customisation, interactor, router)
    }

    private fun feature() =
        FooBarFeature()

    private fun router(
        buildParams: BuildParams<*>,
        connections: FooBarConnections,
        customisation: FooBar.Customisation
    ) =
        FooBarRouter(
            buildParams = buildParams,
            connections = connections,
            transitionHandler = customisation.transitionHandler
        )

    private fun interactor(
        buildParams: BuildParams<*>,
        router: FooBarRouter,
        feature: FooBarFeature
    ) = FooBarInteractor(
        buildParams = buildParams,
        feature = feature,
        router = router
    )

    private fun node(
        buildParams: BuildParams<*>,
        customisation: FooBar.Customisation,
        interactor: FooBarInteractor,
        router: FooBarRouter
    ) = FooBarNode(
        buildParams = buildParams,
        viewFactory = customisation.viewFactory(null),
        plugins = listOf(interactor, router)
    )
}
