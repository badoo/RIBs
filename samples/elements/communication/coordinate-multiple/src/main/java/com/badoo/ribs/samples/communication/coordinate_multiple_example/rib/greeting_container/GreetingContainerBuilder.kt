package com.badoo.ribs.samples.communication.coordinate_multiple_example.rib.greeting_container

import com.badoo.ribs.builder.SimpleBuilder
import com.badoo.ribs.core.modality.BuildParams
import com.badoo.ribs.routing.source.backstack.BackStack
import com.badoo.ribs.samples.communication.coordinate_multiple_example.rib.greeting_container.GreetingContainer.Dependency
import com.badoo.ribs.samples.communication.coordinate_multiple_example.rib.greeting_container.routing.GreetingContainerChildBuilder
import com.badoo.ribs.samples.communication.coordinate_multiple_example.rib.greeting_container.routing.GreetingContainerRouter
import com.badoo.ribs.samples.communication.coordinate_multiple_example.rib.greeting_container.routing.GreetingContainerRouter.Configuration
import com.badoo.ribs.samples.communication.coordinate_multiple_example.rib.greeting_container.routing.GreetingContainerRouter.Configuration.Greeting

class GreetingContainerBuilder(
    private val dependencies: Dependency
) : SimpleBuilder<GreetingContainer>() {

    private val childBuilder by lazy { GreetingContainerChildBuilder(dependencies) }

    private fun interactor(
        buildParams: BuildParams<Nothing?>,
        backStack: BackStack<Configuration>
    ): GreetingContainerInteractor =
        GreetingContainerInteractor(
            buildParams = buildParams,
            backStack = backStack
        )

    override fun build(buildParams: BuildParams<Nothing?>): GreetingContainer {
        val backStack = createBackStack(buildParams)
        val router = createRouter(buildParams, backStack)
        val interactor = interactor(buildParams, backStack)

        return GreetingContainerNode(
            buildParams = buildParams,
            plugins = listOf(router, interactor)
        )
    }

    private fun createBackStack(buildParams: BuildParams<Nothing?>): BackStack<Configuration> =
        BackStack(
            buildParams = buildParams,
            initialConfiguration = Greeting
        )

    private fun createRouter(
        buildParams: BuildParams<Nothing?>,
        backStack: BackStack<Configuration>
    ) = GreetingContainerRouter(
        buildParams = buildParams,
        routingSource = backStack,
        childBuilder = childBuilder
    )
}
