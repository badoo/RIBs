@file:SuppressWarnings("LongParameterList", "LongMethod")
package com.badoo.ribs.example.component.app_bar.builder

import com.badoo.ribs.core.modality.BuildParams
import com.badoo.ribs.example.component.app_bar.AppBar
import com.badoo.ribs.example.component.app_bar.AppBar.Output
import com.badoo.ribs.example.component.app_bar.AppBarInteractor
import com.badoo.ribs.example.component.app_bar.AppBarNode
import com.badoo.ribs.example.component.app_bar.builder.AppBarBuilder.Params
import com.badoo.ribs.example.component.app_bar.routing.AppBarRouter
import com.badoo.ribs.example.component.app_bar.routing.AppBarRouter.Configuration
import com.badoo.ribs.example.component.app_bar.routing.AppBarRouter.Configuration.Content
import com.badoo.ribs.example.component.app_bar.routing.AppBarChildBuilders
import com.badoo.ribs.routing.source.backstack.BackStackFeature
import dagger.Provides
import io.reactivex.functions.Consumer

@dagger.Module
internal object AppBarModule {

    @AppBarScope
    @Provides
    @JvmStatic
    internal fun backStack(
        buildParams: BuildParams<Params>
    ): BackStackFeature<Configuration> =
        BackStackFeature(
            buildParams = buildParams,
            initialConfiguration = Content.Default
        )

    @AppBarScope
    @Provides
    @JvmStatic
    internal fun interactor(
        buildParams: BuildParams<Params>,
        output: Consumer<Output>,
        backStack: BackStackFeature<Configuration>
    ): AppBarInteractor =
        AppBarInteractor(
            buildParams = buildParams,
            output = output,
            backStack = backStack
        )

    @AppBarScope
    @Provides
    @JvmStatic
    internal fun childBuilders(
        component: AppBarComponent
    ): AppBarChildBuilders =
        AppBarChildBuilders(
            // child1 = Child1Builder(component)
        )

    @AppBarScope
    @Provides
    @JvmStatic
    internal fun router(
        buildParams: BuildParams<Params>,
        builders: AppBarChildBuilders,
        backStack: BackStackFeature<Configuration>,
        customisation: AppBar.Customisation
    ): AppBarRouter =
        AppBarRouter(
            buildParams = buildParams,
            routingSource = backStack,
            builders = builders,
            transitionHandler = customisation.transitionHandler
        )

    @AppBarScope
    @Provides
    @JvmStatic
    internal fun node(
        buildParams: BuildParams<Params>,
        customisation: AppBar.Customisation,
        interactor: AppBarInteractor,
        router: AppBarRouter
    ) : AppBarNode = AppBarNode(
        buildParams = buildParams,
        viewFactory = customisation.viewFactory(null),
        plugins = listOf(interactor, router)
    )
}
