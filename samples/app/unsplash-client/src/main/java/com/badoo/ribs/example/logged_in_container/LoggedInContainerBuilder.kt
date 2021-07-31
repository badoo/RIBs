package com.badoo.ribs.example.logged_in_container

import com.badoo.ribs.builder.SimpleBuilder
import com.badoo.ribs.core.modality.BuildParams
import com.badoo.ribs.example.logged_in_container.routing.LoggedInContainerChildBuilders
import com.badoo.ribs.example.logged_in_container.routing.LoggedInContainerRouter
import com.badoo.ribs.example.logged_in_container.routing.LoggedInContainerRouter.Configuration.Content
import com.badoo.ribs.routing.source.backstack.BackStack

class LoggedInContainerBuilder(
    private val dependency: LoggedInContainer.Dependency
) : SimpleBuilder<LoggedInContainer>() {

    private val builders = LoggedInContainerChildBuilders(dependency)

    override fun build(buildParams: BuildParams<Nothing?>): LoggedInContainer {
        val customisation = buildParams.getOrDefault(LoggedInContainer.Customisation())
        val backStack = BackStack<LoggedInContainerRouter.Configuration>(
            buildParams = buildParams,
            initialConfiguration = Content.PhotoFeed
        )
        val interactor = LoggedInContainerInteractor(
            buildParams = buildParams,
            backStack = backStack,
            portal = dependency.portal
        )
        val router = LoggedInContainerRouter(
            buildParams = buildParams,
            routingSource = backStack,
            builders = builders,
            transitionHandler = customisation.transitionHandler
        )

        return LoggedInContainerNode(
            buildParams = buildParams,
            plugins = listOf(interactor, router)
        )
    }

}
