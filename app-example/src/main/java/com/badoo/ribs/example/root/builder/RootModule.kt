@file:SuppressWarnings("LongParameterList", "LongMethod")

package com.badoo.ribs.example.root.builder

import com.badoo.ribs.core.modality.BuildParams
import com.badoo.ribs.example.auth.AuthDataSource
import com.badoo.ribs.example.auth.AuthDataSourceImpl
import com.badoo.ribs.example.auth.AuthStateStorage
import com.badoo.ribs.example.logged_in_container.builder.LoggedInContainerBuilder
import com.badoo.ribs.example.logged_out_container.builder.LoggedOutContainerBuilder
import com.badoo.ribs.example.network.UnsplashApi
import com.badoo.ribs.example.root.Root
import com.badoo.ribs.example.root.RootInteractor
import com.badoo.ribs.example.root.RootNode
import com.badoo.ribs.example.root.routing.RootChildBuilders
import com.badoo.ribs.example.root.routing.RootRouter
import com.badoo.ribs.example.root.routing.RootRouter.Configuration
import com.badoo.ribs.example.root.routing.RootRouter.Configuration.Content
import com.badoo.ribs.routing.source.backstack.BackStackFeature
import dagger.Provides

@dagger.Module
internal object RootModule {

    @RootScope
    @Provides
    @JvmStatic
    internal fun backStack(
        buildParams: BuildParams<Nothing?>
    ): BackStackFeature<Configuration> =
        BackStackFeature(
            buildParams = buildParams,
            initialConfiguration = Content.LoggedOut
        )

    @RootScope
    @Provides
    @JvmStatic
    internal fun authDataSource(
        api: UnsplashApi,
        authStateStorage: AuthStateStorage
    ): AuthDataSource = AuthDataSourceImpl(
        api = api,
        storage = authStateStorage
    )

    @RootScope
    @Provides
    @JvmStatic
    internal fun interactor(
        buildParams: BuildParams<Nothing?>,
        backStack: BackStackFeature<Configuration>,
        authDataSource: AuthDataSource
    ): RootInteractor =
        RootInteractor(
            buildParams = buildParams,
            backStack = backStack,
            authDataSource = authDataSource
        )

    @RootScope
    @Provides
    @JvmStatic
    internal fun childBuilders(
        component: RootComponent
    ): RootChildBuilders =
        RootChildBuilders(
            loggedInContainerBuilder = LoggedInContainerBuilder(component),
            loggedOutContainerBuilder = LoggedOutContainerBuilder(component)
        )

    @RootScope
    @Provides
    @JvmStatic
    internal fun router(
        buildParams: BuildParams<Nothing?>,
        builders: RootChildBuilders,
        backStack: BackStackFeature<Configuration>,
        customisation: Root.Customisation
    ): RootRouter =
        RootRouter(
            buildParams = buildParams,
            routingSource = backStack,
            builders = builders,
            transitionHandler = customisation.transitionHandler
        )

    @RootScope
    @Provides
    @JvmStatic
    internal fun node(
        buildParams: BuildParams<Nothing?>,
        interactor: RootInteractor,
        router: RootRouter
    ): RootNode = RootNode(
        buildParams = buildParams,
        plugins = listOf(interactor, router)
    )
}
