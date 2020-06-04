@file:SuppressWarnings("LongParameterList")

package com.badoo.ribs.template.node.foo_bar

import com.badoo.ribs.builder.SimpleBuilder
import com.badoo.ribs.core.modality.BuildParams
import com.badoo.ribs.routing.source.RoutingSource
import com.badoo.ribs.routing.source.backstack.BackStackFeature
import com.badoo.ribs.template.node.foo_bar.feature.FooBarFeature
import com.badoo.ribs.template.node.foo_bar.routing.FooBarChildBuilders
import com.badoo.ribs.template.node.foo_bar.routing.FooBarRouter
import com.badoo.ribs.template.node.foo_bar.routing.FooBarRouter.Configuration

class FooBarBuilder(
    private val dependency: FooBar.Dependency
) : SimpleBuilder<FooBar>() {

    override fun build(buildParams: BuildParams<Nothing?>): FooBar {
        val connections = FooBarChildBuilders(dependency)
        val customisation = buildParams.getOrDefault(FooBar.Customisation())
        val backStack = backStack(buildParams)
        val feature = feature()
        val interactor = interactor(buildParams, backStack, feature)
        val router = router(buildParams, backStack, connections, customisation)

        return node(buildParams, customisation, interactor, router)
    }

    private fun backStack(buildParams: BuildParams<*>) =
        BackStackFeature<Configuration>(
            buildParams = buildParams,
            initialConfiguration = Configuration.Content.Default
        )

    private fun feature() =
        FooBarFeature()

    private fun interactor(
        buildParams: BuildParams<*>,
        backStack: BackStackFeature<Configuration>,
        feature: FooBarFeature
    ) = FooBarInteractor(
        buildParams = buildParams,
        backStack = backStack,
        feature = feature
    )

    private fun router(
        buildParams: BuildParams<*>,
        routingSource: RoutingSource<Configuration>,
        builders: FooBarChildBuilders,
        customisation: FooBar.Customisation
    ) =
        FooBarRouter(
            buildParams = buildParams,
            builders = builders,
            routingSource = routingSource,
            transitionHandler = customisation.transitionHandler
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
