package com.badoo.ribs.samples.comms_nodes.rib.greeting_container

import com.badoo.ribs.builder.SimpleBuilder
import com.badoo.ribs.core.Node
import com.badoo.ribs.core.modality.BuildParams
import com.badoo.ribs.routing.source.backstack.BackStack
import com.badoo.ribs.samples.comms_nodes.rib.greeting_container.GreetingContainer.Dependency
import com.badoo.ribs.samples.comms_nodes.rib.greeting_container.GreetingContainerRouter.Configuration
import com.badoo.ribs.samples.comms_nodes.rib.greeting_container.GreetingContainerRouter.Configuration.Greeting

class GreetingContainerBuilder(private val dependencies: Dependency) : SimpleBuilder<Node<Nothing>>() {

    override fun build(buildParams: BuildParams<Nothing?>): Node<Nothing> {
        val backStack = createBackStack(buildParams)
        val router = createRouter(buildParams, backStack)
        val presenter = GreetingContainerPresenterImpl(backStack)

        return Node(
            buildParams = buildParams,
            viewFactory = null,
            plugins = listOf(router, presenter)
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
        languages = dependencies.languages
    )
}
