@file:SuppressWarnings("LongParameterList")

package com.badoo.ribs.samples.gallery.rib.android_container

import com.badoo.ribs.builder.SimpleBuilder
import com.badoo.ribs.core.modality.BuildParams
import com.badoo.ribs.routing.source.RoutingSource
import com.badoo.ribs.routing.source.backstack.BackStack
import com.badoo.ribs.samples.gallery.rib.android_container.routing.AndroidContainerChildBuilders
import com.badoo.ribs.samples.gallery.rib.android_container.routing.AndroidContainerRouter
import com.badoo.ribs.samples.gallery.rib.android_container.routing.AndroidContainerRouter.Configuration

class AndroidContainerBuilder(
    private val dependency: AndroidContainer.Dependency
) : SimpleBuilder<AndroidContainer>() {

    override fun build(buildParams: BuildParams<Nothing?>): AndroidContainer {
        val connections = AndroidContainerChildBuilders(dependency)
        val customisation = buildParams.getOrDefault(AndroidContainer.Customisation())
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
    ) = AndroidContainerInteractor(
        buildParams = buildParams,
        backStack = backStack,
    )

    private fun router(
        buildParams: BuildParams<*>,
        routingSource: RoutingSource<Configuration>,
        builders: AndroidContainerChildBuilders,
        customisation: AndroidContainer.Customisation
    ) = AndroidContainerRouter(
            buildParams = buildParams,
            builders = builders,
            routingSource = routingSource,
            transitionHandler = customisation.transitionHandler
        )

    private fun node(
        buildParams: BuildParams<*>,
        customisation: AndroidContainer.Customisation,
        interactor: AndroidContainerInteractor,
        router: AndroidContainerRouter
    ) = AndroidContainerNode(
        buildParams = buildParams,
        viewFactory = customisation.viewFactory(null),
        plugins = listOf(
            interactor,
            router
        ),
    )
}
