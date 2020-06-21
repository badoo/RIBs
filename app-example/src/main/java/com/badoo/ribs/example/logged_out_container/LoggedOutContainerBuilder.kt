package com.badoo.ribs.example.logged_out_container

import com.badoo.ribs.builder.SimpleBuilder
import com.badoo.ribs.core.modality.BuildParams
import com.badoo.ribs.example.auth.AuthDataSource
import com.badoo.ribs.example.logged_out_container.routing.LoggedOutContainerChildBuilders
import com.badoo.ribs.example.logged_out_container.routing.LoggedOutContainerRouter
import com.badoo.ribs.routing.source.backstack.BackStackFeature

class LoggedOutContainerBuilder(
    private val dependency: LoggedOutContainer.Dependency
) : SimpleBuilder<LoggedOutContainer>() {

    override fun build(buildParams: BuildParams<Nothing?>): LoggedOutContainer {
        val connections = LoggedOutContainerChildBuilders()
        val customisation = buildParams.getOrDefault(LoggedOutContainer.Customisation())
        val backStack = backStack(buildParams)
        val interactor = interactor(buildParams, backStack, dependency.authDataSource)
        val router = router(buildParams, connections, backStack, customisation)

        return node(buildParams, interactor, router)
    }


    private fun backStack(
        buildParams: BuildParams<Nothing?>
    ): BackStackFeature<LoggedOutContainerRouter.Configuration> =
        BackStackFeature(
            buildParams = buildParams,
            initialConfiguration = LoggedOutContainerRouter.Configuration.Content.Default
        )

    private fun interactor(
        buildParams: BuildParams<Nothing?>,
        backStack: BackStackFeature<LoggedOutContainerRouter.Configuration>,
        authDataSource: AuthDataSource
    ): LoggedOutContainerInteractor =
        LoggedOutContainerInteractor(
            buildParams = buildParams,
            backStack = backStack,
            authDataSource = authDataSource
        )

    private fun router(
        buildParams: BuildParams<Nothing?>,
        builders: LoggedOutContainerChildBuilders,
        backStack: BackStackFeature<LoggedOutContainerRouter.Configuration>,
        customisation: LoggedOutContainer.Customisation
    ): LoggedOutContainerRouter =
        LoggedOutContainerRouter(
            buildParams = buildParams,
            routingSource = backStack,
            builders = builders,
            transitionHandler = customisation.transitionHandler
        )

    private fun node(
        buildParams: BuildParams<Nothing?>,
        interactor: LoggedOutContainerInteractor,
        router: LoggedOutContainerRouter
    ): LoggedOutContainerNode = LoggedOutContainerNode(
        buildParams = buildParams,
        plugins = listOf(interactor, router)
    )

}
