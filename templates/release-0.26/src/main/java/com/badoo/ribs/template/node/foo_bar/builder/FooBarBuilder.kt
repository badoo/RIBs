@file:SuppressWarnings("LongParameterList")

package com.badoo.ribs.template.node.foo_bar.builder

import com.badoo.ribs.builder.Builder
import com.badoo.ribs.core.modality.BuildParams
import com.badoo.ribs.core.view.RibView
import com.badoo.ribs.routing.source.RoutingSource
import com.badoo.ribs.routing.source.backstack.BackStack
import com.badoo.ribs.rx.disposables
import com.badoo.ribs.template.node.foo_bar.FooBar
import com.badoo.ribs.template.node.foo_bar.FooBarInteractor
import com.badoo.ribs.template.node.foo_bar.FooBarNode
import com.badoo.ribs.template.node.foo_bar.FooBarView
import com.badoo.ribs.template.node.foo_bar.builder.FooBarBuilder.Params
import com.badoo.ribs.template.node.foo_bar.feature.FooBarFeature
import com.badoo.ribs.template.node.foo_bar.routing.FooBarChildBuilders
import com.badoo.ribs.template.node.foo_bar.routing.FooBarRouter
import com.badoo.ribs.template.node.foo_bar.routing.FooBarRouter.Configuration

class FooBarBuilder(
    private val dependency: FooBar.Dependency
) : Builder<Params, FooBar>() {

    class Params

    override fun build(buildParams: BuildParams<Params>): FooBar {
        val connections = FooBarChildBuilders(dependency)
        val customisation = buildParams.getOrDefault(FooBar.Customisation())
        val backStack = backStack(buildParams)
        val feature = feature()
        val interactor: FooBarInteractor =
            interactor(
                buildParams = buildParams,
                backStack = backStack,
                feature = feature,
            )
        val router: FooBarRouter =
            router(
                buildParams = buildParams,
                routingSource = backStack,
                builders = connections,
                customisation = customisation,
            )

        val viewDependency: FooBarView.ViewDependency =
            viewDependency()

        return node(
            buildParams = buildParams,
            viewFactory = customisation.viewFactory(viewDependency),
            feature = feature,
            interactor = interactor,
            router = router,
        )
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
        customisation: FooBar.Customisation
    ) = FooBarRouter(
        buildParams = buildParams,
        builders = builders,
        routingSource = routingSource,
        transitionHandler = customisation.transitionHandler
    )

    private fun viewDependency() =
        object : FooBarView.ViewDependency {

        }

    private fun node(
        buildParams: BuildParams<*>,
        viewFactory: (RibView) -> FooBarView,
        feature: FooBarFeature,
        interactor: FooBarInteractor,
        router: FooBarRouter
    ) = FooBarNode(
        buildParams = buildParams,
        viewFactory = viewFactory,
        plugins = listOf(
            interactor,
            router,
            disposables(feature)
        )
    )
}
