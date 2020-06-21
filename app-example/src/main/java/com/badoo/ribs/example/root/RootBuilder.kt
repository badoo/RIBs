package com.badoo.ribs.example.root

import com.badoo.ribs.builder.SimpleBuilder
import com.badoo.ribs.core.modality.BuildParams
import com.badoo.ribs.example.auth.AuthDataSource
import com.badoo.ribs.example.auth.AuthDataSourceImpl
import com.badoo.ribs.example.root.routing.RootChildBuilders
import com.badoo.ribs.example.root.routing.RootRouter
import com.badoo.ribs.example.root.routing.RootRouter.Configuration
import com.badoo.ribs.routing.source.RoutingSource
import com.badoo.ribs.routing.source.backstack.BackStackFeature

class RootBuilder(
    private val dependency: Root.Dependency
) : SimpleBuilder<Root>() {

    override fun build(buildParams: BuildParams<Nothing?>): Root {
        val authDataSource = authDataSource(dependency)
        val connections = RootChildBuilders(dependency, authDataSource)
        val customisation = buildParams.getOrDefault(Root.Customisation())
        val backStack = backStack(buildParams)
        val interactor = interactor(buildParams, backStack, authDataSource)
        val router = router(buildParams, backStack, connections, customisation)

        return node(buildParams, interactor, router)
    }

    private fun authDataSource(dependency: Root.Dependency): AuthDataSource = AuthDataSourceImpl(
        api = dependency.api,
        storage = dependency.authStateStorage
    )


    private fun backStack(buildParams: BuildParams<*>): BackStackFeature<Configuration> =
        BackStackFeature(
            buildParams = buildParams,
            initialConfiguration = Configuration.Content.LoggedOut
        )


    private fun interactor(
        buildParams: BuildParams<Nothing?>,
        backStack: BackStackFeature<Configuration>,
        authDataSource: AuthDataSource
    ): RootInteractor = RootInteractor(
        buildParams = buildParams,
        backStack = backStack,
        authDataSource = authDataSource
    )

    private fun router(
        buildParams: BuildParams<*>,
        routingSource: RoutingSource<Configuration>,
        builders: RootChildBuilders,
        customisation: Root.Customisation
    ) = RootRouter(
        buildParams = buildParams,
        builders = builders,
        routingSource = routingSource,
        transitionHandler = customisation.transitionHandler
    )

    private fun node(
        buildParams: BuildParams<*>,
        interactor: RootInteractor,
        router: RootRouter
    ) = RootNode(
        buildParams = buildParams,
        plugins = listOf(interactor, router)
    )
}
