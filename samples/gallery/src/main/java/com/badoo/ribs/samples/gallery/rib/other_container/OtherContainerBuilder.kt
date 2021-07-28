@file:SuppressWarnings("LongParameterList")

package com.badoo.ribs.samples.gallery.rib.other_container

import com.badoo.ribs.builder.SimpleBuilder
import com.badoo.ribs.core.modality.BuildParams
import com.badoo.ribs.routing.source.RoutingSource
import com.badoo.ribs.routing.source.backstack.BackStack
import com.badoo.ribs.samples.gallery.rib.other_container.routing.OtherContainerChildBuilders
import com.badoo.ribs.samples.gallery.rib.other_container.routing.OtherContainerRouter
import com.badoo.ribs.samples.gallery.rib.other_container.routing.OtherContainerRouter.Configuration

class OtherContainerBuilder(
    private val dependency: OtherContainer.Dependency
) : SimpleBuilder<OtherContainer>() {

    override fun build(buildParams: BuildParams<Nothing?>): OtherContainer {
        val connections = OtherContainerChildBuilders(dependency)
        val customisation = buildParams.getOrDefault(OtherContainer.Customisation())
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
    ) = OtherContainerInteractor(
        buildParams = buildParams,
        backStack = backStack,
    )

    private fun router(
        buildParams: BuildParams<*>,
        routingSource: RoutingSource<Configuration>,
        builders: OtherContainerChildBuilders,
        customisation: OtherContainer.Customisation
    ) = OtherContainerRouter(
            buildParams = buildParams,
            builders = builders,
            routingSource = routingSource,
            transitionHandler = customisation.transitionHandler
        )

    private fun node(
        buildParams: BuildParams<*>,
        customisation: OtherContainer.Customisation,
        interactor: OtherContainerInteractor,
        router: OtherContainerRouter
    ) = OtherContainerNode(
        buildParams = buildParams,
        viewFactory = customisation.viewFactory(null),
        plugins = listOf(
            interactor,
            router
        ),
    )
}
