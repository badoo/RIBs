package com.badoo.ribs.example.logged_out_container

import com.badoo.ribs.builder.SimpleBuilder
import com.badoo.ribs.core.modality.BuildParams
import com.badoo.ribs.example.logged_out_container.routing.LoggedOutContainerChildBuilders
import com.badoo.ribs.example.logged_out_container.routing.LoggedOutContainerRouter
import com.badoo.ribs.example.logged_out_container.routing.LoggedOutContainerRouter.Configuration.Content
import com.badoo.ribs.routing.source.backstack.BackStack

class LoggedOutContainerBuilder(
    private val dependency: LoggedOutContainer.Dependency
) : SimpleBuilder<LoggedOutContainer>() {

    private val builders = LoggedOutContainerChildBuilders(dependency)

    override fun build(buildParams: BuildParams<Nothing?>): LoggedOutContainer {
        val customisation = buildParams.getOrDefault(LoggedOutContainer.Customisation())
        val backStack = BackStack<LoggedOutContainerRouter.Configuration>(
            buildParams = buildParams,
            initialConfiguration = Content.Welcome
        )
        val interactor = LoggedOutContainerInteractor(
            buildParams = buildParams,
            backStack = backStack,
            authDataSource = dependency.authDataSource
        )
        val router = LoggedOutContainerRouter(
            buildParams = buildParams,
            routingSource = backStack,
            builders = builders,
            transitionHandler = customisation.transitionHandler
        )

        return LoggedOutContainerNode(
            buildParams = buildParams,
            plugins = listOf(interactor, router)
        )
    }


}
