package com.badoo.ribs.samples.routing.parameterised_routing.rib.parameterised_routing_example

import com.badoo.ribs.builder.SimpleBuilder
import com.badoo.ribs.core.modality.BuildParams
import com.badoo.ribs.routing.source.backstack.BackStack
import com.badoo.ribs.samples.routing.parameterised_routing.rib.parameterised_routing_example.ParameterisedRoutingExampleRouter.Configuration
import com.badoo.ribs.samples.routing.parameterised_routing.rib.profile.ProfileBuilder

class ParameterisedRoutingExampleBuilder(
    private val dependency: ParameterisedRoutingExample.Dependency
) : SimpleBuilder<ParameterisedRoutingExample>() {

    override fun build(buildParams: BuildParams<Nothing?>): ParameterisedRoutingExample {
        val backStack: BackStack<Configuration> = BackStack(
            initialConfiguration = Configuration.Default,
            buildParams = buildParams
        )
        val presenter = ParameterisedRoutingExamplePresenterImpl(
            backStack = backStack
        )
        val router = ParameterisedRoutingExampleRouter(
            buildParams = buildParams,
            routingSource = backStack,
            profileBuilder = ProfileBuilder()
        )
        val viewDependencies = object : ParameterisedRoutingExampleView.Dependency {
            override val presenter: ParameterisedRoutingExamplePresenter = presenter
        }
        return ParameterisedRoutingExampleNode(
            buildParams = buildParams,
            viewFactory = ParameterisedRoutingExampleViewImpl.Factory().invoke(deps = viewDependencies),
            plugins = listOf(presenter, router)
        )
    }
}
