@file:SuppressWarnings("LongParameterList")

package com.badoo.ribs.samples.gallery.rib.communication.container

import com.badoo.ribs.builder.SimpleBuilder
import com.badoo.ribs.core.modality.BuildParams
import com.badoo.ribs.routing.source.RoutingSource
import com.badoo.ribs.routing.source.backstack.BackStack
import com.badoo.ribs.samples.gallery.rib.communication.container.routing.CommunicationContainerChildBuilders
import com.badoo.ribs.samples.gallery.rib.communication.container.routing.CommunicationContainerRouter
import com.badoo.ribs.samples.gallery.rib.communication.container.routing.CommunicationContainerRouter.Configuration

class CommunicationContainerBuilder(
    private val dependency: CommunicationContainer.Dependency
) : SimpleBuilder<CommunicationContainer>() {

    override fun build(buildParams: BuildParams<Nothing?>): CommunicationContainer {
        val connections = CommunicationContainerChildBuilders(dependency)
        val customisation = buildParams.getOrDefault(CommunicationContainer.Customisation())
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
    ) = CommunicationContainerInteractor(
        buildParams = buildParams,
        backStack = backStack,
    )

    private fun router(
        buildParams: BuildParams<*>,
        routingSource: RoutingSource<Configuration>,
        builders: CommunicationContainerChildBuilders,
        customisation: CommunicationContainer.Customisation
    ) = CommunicationContainerRouter(
            buildParams = buildParams,
            builders = builders,
            routingSource = routingSource,
            transitionHandler = customisation.transitionHandler
        )

    private fun node(
        buildParams: BuildParams<*>,
        customisation: CommunicationContainer.Customisation,
        interactor: CommunicationContainerInteractor,
        router: CommunicationContainerRouter
    ) = CommunicationContainerNode(
        buildParams = buildParams,
        viewFactory = customisation.viewFactory(null),
        plugins = listOf(
            interactor,
            router
        ),
    )
}
