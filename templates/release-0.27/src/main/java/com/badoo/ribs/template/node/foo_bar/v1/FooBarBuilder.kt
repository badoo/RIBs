@file:SuppressWarnings("LongParameterList")

package com.badoo.ribs.template.node.foo_bar.v1

import com.badoo.ribs.builder.SimpleBuilder
import com.badoo.ribs.core.modality.BuildParams
import com.badoo.ribs.routing.source.RoutingSource
import com.badoo.ribs.routing.source.backstack.BackStack
import com.badoo.ribs.rx2.disposables
import com.badoo.ribs.template.node.foo_bar.common.FooBar
import com.badoo.ribs.template.node.foo_bar.common.feature.FooBarFeature
import com.badoo.ribs.template.node.foo_bar.v1.routing.FooBarChildBuilders
import com.badoo.ribs.template.node.foo_bar.v1.routing.FooBarRouter
import com.badoo.ribs.template.node.foo_bar.v1.routing.FooBarRouter.Configuration

class FooBarBuilder(
    private val dependency: FooBar.Dependency
) : SimpleBuilder<FooBarRib>() {

    override fun build(buildParams: BuildParams<Nothing?>): FooBarRib {
        val connections = FooBarChildBuilders(dependency)
        val customisation = buildParams.getOrDefault(FooBarRib.Customisation())
        val backStack = backStack(buildParams)
        val feature = feature()
        val interactor = interactor(buildParams, backStack, feature)
        val router = router(buildParams, backStack, connections, customisation)

        return node(buildParams, customisation, feature, interactor, router)
    }

    private fun backStack(buildParams: BuildParams<*>) =
        BackStack<Configuration>(
            buildParams = buildParams,
            initialConfiguration = Configuration.Content.Default
        )

    private fun feature() =
        FooBarFeature()

    private fun interactor(
        buildParams: BuildParams<*>,
        backStack: BackStack<Configuration>,
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
        customisation: FooBarRib.Customisation
    ) = FooBarRouter(
            buildParams = buildParams,
            builders = builders,
            routingSource = routingSource,
            transitionHandler = customisation.transitionHandler
        )

    private fun node(
        buildParams: BuildParams<*>,
        customisation: FooBarRib.Customisation,
        feature: FooBarFeature,
        interactor: FooBarInteractor,
        router: FooBarRouter
    ) = FooBarNode(
        buildParams = buildParams,
        viewFactory = customisation.viewFactory(null),
        plugins = listOf(interactor, router, disposables(feature))
    )
}
