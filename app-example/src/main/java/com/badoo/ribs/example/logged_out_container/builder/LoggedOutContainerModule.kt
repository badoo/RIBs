@file:SuppressWarnings("LongParameterList", "LongMethod")

package com.badoo.ribs.example.logged_out_container.builder

import com.badoo.ribs.core.modality.BuildParams
import com.badoo.ribs.example.auth.AuthDataSource
import com.badoo.ribs.example.logged_out_container.LoggedOutContainer
import com.badoo.ribs.example.logged_out_container.LoggedOutContainerInteractor
import com.badoo.ribs.example.logged_out_container.LoggedOutContainerNode
import com.badoo.ribs.example.logged_out_container.routing.LoggedOutContainerChildBuilders
import com.badoo.ribs.example.logged_out_container.routing.LoggedOutContainerRouter
import com.badoo.ribs.example.logged_out_container.routing.LoggedOutContainerRouter.Configuration
import com.badoo.ribs.example.logged_out_container.routing.LoggedOutContainerRouter.Configuration.Content
import com.badoo.ribs.routing.source.backstack.BackStackFeature
import dagger.Provides

@dagger.Module
internal object LoggedOutContainerModule {

    @LoggedOutContainerScope
    @Provides
    @JvmStatic
    internal fun backStack(
        buildParams: BuildParams<Nothing?>
    ): BackStackFeature<Configuration> =
        BackStackFeature(
            buildParams = buildParams,
            initialConfiguration = Content.Default
        )

    @LoggedOutContainerScope
    @Provides
    @JvmStatic
    internal fun interactor(
        buildParams: BuildParams<Nothing?>,
        backStack: BackStackFeature<Configuration>,
        authDataSource: AuthDataSource
    ): LoggedOutContainerInteractor =
        LoggedOutContainerInteractor(
            buildParams = buildParams,
            backStack = backStack,
            authDataSource = authDataSource
        )

    @LoggedOutContainerScope
    @Provides
    @JvmStatic
    internal fun childBuilders(
        component: LoggedOutContainerComponent
    ): LoggedOutContainerChildBuilders =
        LoggedOutContainerChildBuilders(
            // child1 = Child1Builder(component)
        )

    @LoggedOutContainerScope
    @Provides
    @JvmStatic
    internal fun router(
        buildParams: BuildParams<Nothing?>,
        builders: LoggedOutContainerChildBuilders,
        backStack: BackStackFeature<Configuration>,
        customisation: LoggedOutContainer.Customisation
    ): LoggedOutContainerRouter =
        LoggedOutContainerRouter(
            buildParams = buildParams,
            routingSource = backStack,
            builders = builders,
            transitionHandler = customisation.transitionHandler
        )

    @LoggedOutContainerScope
    @Provides
    @JvmStatic
    internal fun node(
        buildParams: BuildParams<Nothing?>,
        interactor: LoggedOutContainerInteractor,
        router: LoggedOutContainerRouter
    ): LoggedOutContainerNode = LoggedOutContainerNode(
        buildParams = buildParams,
        plugins = listOf(interactor, router)
    )
}
