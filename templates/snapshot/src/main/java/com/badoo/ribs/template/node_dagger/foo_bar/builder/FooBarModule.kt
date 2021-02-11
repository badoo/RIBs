@file:SuppressWarnings("LongParameterList", "LongMethod")
package com.badoo.ribs.template.node_dagger.foo_bar.builder

import com.badoo.ribs.core.modality.BuildParams
import com.badoo.ribs.routing.source.backstack.BackStack
import com.badoo.ribs.rx2.disposables
import com.badoo.ribs.template.node_dagger.foo_bar.FooBar
import com.badoo.ribs.template.node_dagger.foo_bar.FooBarInteractor
import com.badoo.ribs.template.node_dagger.foo_bar.FooBarNode
import com.badoo.ribs.template.node_dagger.foo_bar.feature.FooBarFeature
import com.badoo.ribs.template.node_dagger.foo_bar.routing.FooBarChildBuilders
import com.badoo.ribs.template.node_dagger.foo_bar.routing.FooBarRouter
import com.badoo.ribs.template.node_dagger.foo_bar.routing.FooBarRouter.Configuration
import com.badoo.ribs.template.node_dagger.foo_bar.routing.FooBarRouter.Configuration.Content
import dagger.Provides

@dagger.Module
internal object FooBarModule {

    @FooBarScope
    @Provides
    @JvmStatic
    internal fun feature(): FooBarFeature =
        FooBarFeature()

    @FooBarScope
    @Provides
    @JvmStatic
    internal fun backStack(
        buildParams: BuildParams<Nothing?>
    ): BackStack<Configuration> =
        BackStack(
            buildParams = buildParams,
            initialConfiguration = Content.Default
        )

    @FooBarScope
    @Provides
    @JvmStatic
    internal fun interactor(
        buildParams: BuildParams<Nothing?>,
        feature: FooBarFeature,
        backStack: BackStack<Configuration>
    ): FooBarInteractor =
        FooBarInteractor(
            buildParams = buildParams,
            feature = feature,
            backStack = backStack
        )

    @FooBarScope
    @Provides
    @JvmStatic
    internal fun childBuilders(
        component: FooBarComponent
    ): FooBarChildBuilders =
        FooBarChildBuilders(
            // child1 = Child1Builder(component)
        )

    @FooBarScope
    @Provides
    @JvmStatic
    internal fun router(
        buildParams: BuildParams<Nothing?>,
        builders: FooBarChildBuilders,
        backStack: BackStack<Configuration>,
        customisation: FooBar.Customisation
    ): FooBarRouter =
        FooBarRouter(
            buildParams = buildParams,
            routingSource = backStack,
            builders = builders,
            transitionHandler = customisation.transitionHandler
        )

    @FooBarScope
    @Provides
    @JvmStatic
    internal fun node(
        buildParams: BuildParams<Nothing?>,
        customisation: FooBar.Customisation,
        feature: FooBarFeature,
        interactor: FooBarInteractor,
        router: FooBarRouter
    ) : FooBarNode = FooBarNode(
        buildParams = buildParams,
        viewFactory = customisation.viewFactory(null),
        plugins = listOf(interactor, router, disposables(feature))
    )
}
