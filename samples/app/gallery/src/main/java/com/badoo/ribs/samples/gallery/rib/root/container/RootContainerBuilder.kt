@file:SuppressWarnings("LongParameterList")

package com.badoo.ribs.samples.gallery.rib.root.container

import com.badoo.ribs.builder.SimpleBuilder
import com.badoo.ribs.core.modality.BuildParams
import com.badoo.ribs.routing.source.RoutingSource
import com.badoo.ribs.routing.source.backstack.BackStack
import com.badoo.ribs.samples.gallery.rib.root.container.routing.RootContainerChildBuilders
import com.badoo.ribs.samples.gallery.rib.root.container.routing.RootContainerRouter
import com.badoo.ribs.samples.gallery.rib.root.container.routing.RootContainerRouter.Configuration

class RootContainerBuilder(
    private val dependency: RootContainer.Dependency
) : SimpleBuilder<RootContainer>() {

    override fun build(buildParams: BuildParams<Nothing?>): RootContainer {
        val connections = RootContainerChildBuilders(dependency)
        val customisation = buildParams.getOrDefault(RootContainer.Customisation())
        val backStack = backStack(buildParams)
        val interactor = interactor(buildParams, backStack)
        val router = router(buildParams, backStack, connections, customisation)

        return node(buildParams, customisation, interactor, router, backStack)
    }

    private fun backStack(buildParams: BuildParams<*>) =
        BackStack<Configuration>(
            buildParams = buildParams,
            initialConfiguration = Configuration.Picker
        )

    private fun interactor(
        buildParams: BuildParams<*>,
        backStack: BackStack<Configuration>,
    ) = RootContainerInteractor(
        buildParams = buildParams,
        backStack = backStack
    )

    private fun router(
        buildParams: BuildParams<*>,
        routingSource: RoutingSource<Configuration>,
        builders: RootContainerChildBuilders,
        customisation: RootContainer.Customisation
    ) = RootContainerRouter(
        buildParams = buildParams,
        builders = builders,
        routingSource = routingSource,
        transitionHandler = customisation.transitionHandler
    )

    private fun node(
        buildParams: BuildParams<*>,
        customisation: RootContainer.Customisation,
        interactor: RootContainerInteractor,
        router: RootContainerRouter,
        backStack: BackStack<Configuration>
    ) = RootContainerNode(
        buildParams = buildParams,
        viewFactory = customisation.viewFactory(null),
        backStack = backStack,
        plugins = listOf(
            interactor,
            router
        ),
    )
}
