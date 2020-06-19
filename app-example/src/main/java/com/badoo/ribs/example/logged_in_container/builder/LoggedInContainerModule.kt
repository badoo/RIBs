@file:SuppressWarnings("LongParameterList", "LongMethod")

package com.badoo.ribs.example.logged_in_container.builder

import com.badoo.ribs.core.modality.BuildParams
import com.badoo.ribs.example.auth.AuthDataSource
import com.badoo.ribs.example.logged_in_container.LoggedInContainer
import com.badoo.ribs.example.logged_in_container.LoggedInContainerInteractor
import com.badoo.ribs.example.logged_in_container.LoggedInContainerNode
import com.badoo.ribs.example.logged_in_container.routing.LoggedInContainerChildBuilders
import com.badoo.ribs.example.logged_in_container.routing.LoggedInContainerRouter
import com.badoo.ribs.example.logged_in_container.routing.LoggedInContainerRouter.Configuration
import com.badoo.ribs.example.logged_in_container.routing.LoggedInContainerRouter.Configuration.Content
import com.badoo.ribs.routing.source.backstack.BackStackFeature
import dagger.Provides

@dagger.Module
internal object LoggedInContainerModule {

    @LoggedInContainerScope
    @Provides
    @JvmStatic
    internal fun backStack(
        buildParams: BuildParams<Nothing?>
    ): BackStackFeature<Configuration> =
        BackStackFeature(
            buildParams = buildParams,
            initialConfiguration = Content.Default
        )

    @LoggedInContainerScope
    @Provides
    @JvmStatic
    internal fun interactor(
        buildParams: BuildParams<Nothing?>,
        backStack: BackStackFeature<Configuration>,
        authDataSource: AuthDataSource
    ): LoggedInContainerInteractor =
        LoggedInContainerInteractor(
            buildParams = buildParams,
            backStack = backStack,
            authDataSource = authDataSource
        )

    @LoggedInContainerScope
    @Provides
    @JvmStatic
    internal fun childBuilders(
        component: LoggedInContainerComponent
    ): LoggedInContainerChildBuilders =
        LoggedInContainerChildBuilders(
            // child1 = Child1Builder(component)
        )

    @LoggedInContainerScope
    @Provides
    @JvmStatic
    internal fun router(
        buildParams: BuildParams<Nothing?>,
        builders: LoggedInContainerChildBuilders,
        backStack: BackStackFeature<Configuration>,
        customisation: LoggedInContainer.Customisation
    ): LoggedInContainerRouter =
        LoggedInContainerRouter(
            buildParams = buildParams,
            routingSource = backStack,
            builders = builders,
            transitionHandler = customisation.transitionHandler
        )

    @LoggedInContainerScope
    @Provides
    @JvmStatic
    internal fun node(
        buildParams: BuildParams<Nothing?>,
        interactor: LoggedInContainerInteractor,
        router: LoggedInContainerRouter
    ): LoggedInContainerNode = LoggedInContainerNode(
        buildParams = buildParams,
        plugins = listOf(interactor, router)
    )
}
