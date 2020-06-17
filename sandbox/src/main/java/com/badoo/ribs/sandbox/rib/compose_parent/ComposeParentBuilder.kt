@file:SuppressWarnings("LongParameterList")

package com.badoo.ribs.sandbox.rib.compose_parent

import com.badoo.ribs.builder.SimpleBuilder
import com.badoo.ribs.core.modality.BuildParams
import com.badoo.ribs.routing.source.RoutingSource
import com.badoo.ribs.routing.source.backstack.BackStackFeature
import com.badoo.ribs.sandbox.rib.compose_parent.feature.ComposeParentFeature
import com.badoo.ribs.sandbox.rib.compose_parent.routing.ComposeParentChildBuilders
import com.badoo.ribs.sandbox.rib.compose_parent.routing.ComposeParentRouter
import com.badoo.ribs.sandbox.rib.compose_parent.routing.ComposeParentRouter.Configuration

class ComposeParentBuilder(
    private val dependency: ComposeParent.Dependency
) : SimpleBuilder<ComposeParent>() {

    override fun build(buildParams: BuildParams<Nothing?>): ComposeParent {
        val connections = ComposeParentChildBuilders(dependency)
        val customisation = buildParams.getOrDefault(ComposeParent.Customisation())
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
        ComposeParentFeature()

    private fun interactor(
        buildParams: BuildParams<*>,
        backStack: BackStackFeature<Configuration>,
        feature: ComposeParentFeature
    ) = ComposeParentInteractor(
        buildParams = buildParams,
        backStack = backStack,
        feature = feature
    )

    private fun router(
        buildParams: BuildParams<*>,
        routingSource: RoutingSource<Configuration>,
        builders: ComposeParentChildBuilders,
        customisation: ComposeParent.Customisation
    ) =
        ComposeParentRouter(
            buildParams = buildParams,
            builders = builders,
            routingSource = routingSource,
            transitionHandler = customisation.transitionHandler
        )

    private fun node(
        buildParams: BuildParams<*>,
        customisation: ComposeParent.Customisation,
        interactor: ComposeParentInteractor,
        router: ComposeParentRouter
    ) = ComposeParentNode(
        buildParams = buildParams,
        viewFactory = customisation.viewFactory(null),
        plugins = listOf(interactor, router)
    )
}
