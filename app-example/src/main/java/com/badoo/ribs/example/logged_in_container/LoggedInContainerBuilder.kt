package com.badoo.ribs.example.logged_in_container

import com.badoo.ribs.builder.SimpleBuilder
import com.badoo.ribs.core.modality.BuildParams
import com.badoo.ribs.example.auth.AuthDataSource
import com.badoo.ribs.example.logged_in_container.routing.LoggedInContainerChildBuilders
import com.badoo.ribs.example.logged_in_container.routing.LoggedInContainerRouter
import com.badoo.ribs.routing.source.backstack.BackStackFeature

class LoggedInContainerBuilder(
    private val dependency: LoggedInContainer.Dependency
) : SimpleBuilder<LoggedInContainer>() {

    override fun build(buildParams: BuildParams<Nothing?>): LoggedInContainer {
        val connections = LoggedInContainerChildBuilders()
        val customisation = buildParams.getOrDefault(LoggedInContainer.Customisation())
        val backStack = backStack(buildParams)
        val interactor = interactor(buildParams, backStack, dependency.authDataSource)
        val router = router(buildParams, connections, backStack, customisation)

        return node(buildParams, interactor, router)
    }


    private fun backStack(
        buildParams: BuildParams<Nothing?>
    ): BackStackFeature<LoggedInContainerRouter.Configuration> =
        BackStackFeature(
            buildParams = buildParams,
            initialConfiguration = LoggedInContainerRouter.Configuration.Content.Default
        )

    private fun interactor(
        buildParams: BuildParams<Nothing?>,
        backStack: BackStackFeature<LoggedInContainerRouter.Configuration>,
        authDataSource: AuthDataSource
    ): LoggedInContainerInteractor =
        LoggedInContainerInteractor(
            buildParams = buildParams,
            backStack = backStack,
            authDataSource = authDataSource
        )

    private fun router(
        buildParams: BuildParams<Nothing?>,
        builders: LoggedInContainerChildBuilders,
        backStack: BackStackFeature<LoggedInContainerRouter.Configuration>,
        customisation: LoggedInContainer.Customisation
    ): LoggedInContainerRouter =
        LoggedInContainerRouter(
            buildParams = buildParams,
            routingSource = backStack,
            builders = builders,
            transitionHandler = customisation.transitionHandler
        )

    private fun node(
        buildParams: BuildParams<Nothing?>,
        interactor: LoggedInContainerInteractor,
        router: LoggedInContainerRouter
    ): LoggedInContainerNode = LoggedInContainerNode(
        buildParams = buildParams,
        plugins = listOf(interactor, router)
    )

}
