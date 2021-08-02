package com.badoo.ribs.example.root

import com.badoo.ribs.builder.SimpleBuilder
import com.badoo.ribs.core.modality.BuildParams
import com.badoo.ribs.example.auth.AuthDataSourceImpl
import com.badoo.ribs.example.root.routing.RootChildBuilders
import com.badoo.ribs.example.root.routing.RootRouter
import com.badoo.ribs.example.root.routing.RootRouter.Configuration
import com.badoo.ribs.routing.source.backstack.BackStack

class RootBuilder(
    private val dependency: Root.Dependency
) : SimpleBuilder<Root>() {

    private val authDataSource = AuthDataSourceImpl(
        api = dependency.api,
        storage = dependency.authStateStorage
    )
    private val builders = RootChildBuilders(dependency, authDataSource)

    override fun build(buildParams: BuildParams<Nothing?>): Root {
        val customisation = buildParams.getOrDefault(Root.Customisation())
        val backStack = BackStack<Configuration>(
            buildParams = buildParams,
            initialConfiguration = Configuration.Content.LoggedOut
        )
        val interactor = RootInteractor(
            buildParams = buildParams,
            backStack = backStack,
            authDataSource = authDataSource,
            networkErrors = dependency.networkErrors
        )
        val router = RootRouter(
            buildParams = buildParams,
            builders = builders,
            routingSource = backStack,
            transitionHandler = customisation.transitionHandler
        )

        return RootNode(
            buildParams = buildParams,
            plugins = listOf(interactor, router)
        )
    }

}
