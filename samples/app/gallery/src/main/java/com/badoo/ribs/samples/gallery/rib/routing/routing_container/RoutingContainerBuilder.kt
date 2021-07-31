@file:SuppressWarnings("LongParameterList")

package com.badoo.ribs.samples.gallery.rib.routing.routing_container

import com.badoo.ribs.builder.SimpleBuilder
import com.badoo.ribs.core.modality.BuildParams
import com.badoo.ribs.routing.source.RoutingSource
import com.badoo.ribs.routing.source.backstack.BackStack
import com.badoo.ribs.samples.gallery.rib.routing.routing_container.routing.RoutingContainerChildBuilders
import com.badoo.ribs.samples.gallery.rib.routing.routing_container.routing.RoutingContainerRouter
import com.badoo.ribs.samples.gallery.rib.routing.routing_container.routing.RoutingContainerRouter.Configuration

class RoutingContainerBuilder(
    private val dependency: RoutingContainer.Dependency
) : SimpleBuilder<RoutingContainer>() {

    override fun build(buildParams: BuildParams<Nothing?>): RoutingContainer {
        val connections = RoutingContainerChildBuilders(dependency)
        val customisation = buildParams.getOrDefault(RoutingContainer.Customisation())
        val backStack = backStack(buildParams)
        val interactor = interactor(buildParams, backStack)
        val router = router(buildParams, backStack, connections, customisation)

        return node(buildParams, customisation, interactor, router)
    }

    private fun backStack(buildParams: BuildParams<*>) =
        BackStack<Configuration>(
            buildParams = buildParams,
            initialConfiguration = Configuration.Picker
        )

    private fun interactor(
        buildParams: BuildParams<*>,
        backStack: BackStack<Configuration>,
    ) = RoutingContainerInteractor(
        buildParams = buildParams,
        backStack = backStack,
    )

    private fun router(
        buildParams: BuildParams<*>,
        routingSource: RoutingSource<Configuration>,
        builders: RoutingContainerChildBuilders,
        customisation: RoutingContainer.Customisation
    ) = RoutingContainerRouter(
            buildParams = buildParams,
            builders = builders,
            routingSource = routingSource,
            transitionHandler = customisation.transitionHandler
        )

    private fun node(
        buildParams: BuildParams<*>,
        customisation: RoutingContainer.Customisation,
        interactor: RoutingContainerInteractor,
        router: RoutingContainerRouter
    ) = RoutingContainerNode(
        buildParams = buildParams,
        viewFactory = customisation.viewFactory(null),
        plugins = listOf(
            interactor,
            router
        ),
    )
}
