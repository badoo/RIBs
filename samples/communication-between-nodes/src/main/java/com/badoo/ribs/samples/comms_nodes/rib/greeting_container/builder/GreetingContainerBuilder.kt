package com.badoo.ribs.samples.comms_nodes.rib.greeting_container.builder

import com.badoo.ribs.builder.SimpleBuilder
import com.badoo.ribs.core.Node
import com.badoo.ribs.core.modality.BuildParams
import com.badoo.ribs.routing.source.backstack.BackStack
import com.badoo.ribs.samples.comms_nodes.app.Language
import com.badoo.ribs.samples.comms_nodes.rib.greeting.builder.GreetingBuilder
import com.badoo.ribs.samples.comms_nodes.rib.greeting_container.GreetingContainerPresenterImpl
import com.badoo.ribs.samples.comms_nodes.rib.greeting_container.GreetingContainerRouter

class GreetingContainerBuilder(private val languages: List<Language>) : SimpleBuilder<Node<Nothing>>() {

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

    private fun createBackStack(buildParams: BuildParams<Nothing?>): BackStack<GreetingContainerRouter.Configuration> =
        BackStack(
            buildParams = buildParams,
            initialConfiguration = GreetingContainerRouter.Configuration.Greeting
        )

    private fun createRouter(
        buildParams: BuildParams<Nothing?>,
        backStack: BackStack<GreetingContainerRouter.Configuration>
    ) = GreetingContainerRouter(
        buildParams = buildParams,
        routingSource = backStack,
        greetingBuilder = GreetingBuilder(),
        languages = languages
    )
}
