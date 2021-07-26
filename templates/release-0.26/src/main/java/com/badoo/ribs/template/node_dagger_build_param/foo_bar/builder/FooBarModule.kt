@file:SuppressWarnings("LongParameterList", "LongMethod")

package com.badoo.ribs.template.node_dagger_build_param.foo_bar.builder

import com.badoo.ribs.core.modality.BuildParams
import com.badoo.ribs.routing.source.backstack.BackStack
import com.badoo.ribs.rx.disposables
import com.badoo.ribs.template.node_dagger_build_param.foo_bar.FooBar
import com.badoo.ribs.template.node_dagger_build_param.foo_bar.FooBarInteractor
import com.badoo.ribs.template.node_dagger_build_param.foo_bar.FooBarNode
import com.badoo.ribs.template.node_dagger_build_param.foo_bar.FooBarView
import com.badoo.ribs.template.node_dagger_build_param.foo_bar.builder.FooBarBuilder.Params
import com.badoo.ribs.template.node_dagger_build_param.foo_bar.feature.FooBarFeature
import com.badoo.ribs.template.node_dagger_build_param.foo_bar.routing.FooBarChildBuilders
import com.badoo.ribs.template.node_dagger_build_param.foo_bar.routing.FooBarRouter
import com.badoo.ribs.template.node_dagger_build_param.foo_bar.routing.FooBarRouter.Configuration
import com.badoo.ribs.template.node_dagger_build_param.foo_bar.routing.FooBarRouter.Configuration.Content
import dagger.Provides

@dagger.Module
internal object FooBarModule {

    @FooBarScope
    @Provides
    internal fun feature(): FooBarFeature =
        FooBarFeature()

    @FooBarScope
    @Provides
    internal fun backStack(
        buildParams: BuildParams<Params>
    ): BackStack<Configuration> =
        BackStack(
            buildParams = buildParams,
            initialConfiguration = Content.Default
        )

    @FooBarScope
    @Provides
    internal fun interactor(
        dependency: FooBar.Dependency,
        buildParams: BuildParams<Params>,
        backStack: BackStack<Configuration>,
        feature: FooBarFeature
    ): FooBarInteractor =
        FooBarInteractor(
            buildParams = buildParams,
            backStack = backStack,
            feature = feature
        )

    @FooBarScope
    @Provides
    internal fun childBuilders(
        dependency: FooBar.Dependency
    ): FooBarChildBuilders =
        FooBarChildBuilders(
            dependency,
        )

    @FooBarScope
    @Provides
    internal fun router(
        buildParams: BuildParams<Params>,
        backStack: BackStack<Configuration>,
        builders: FooBarChildBuilders,
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
    internal fun viewDependency(): FooBarView.ViewDependency =
        object : FooBarView.ViewDependency {

        }

    @FooBarScope
    @Provides
    internal fun node(
        buildParams: BuildParams<Params>,
        customisation: FooBar.Customisation,
        interactor: FooBarInteractor,
        router: FooBarRouter,
        viewDependency: FooBarView.ViewDependency,
        feature: FooBarFeature,
    ): FooBarNode = FooBarNode(
        buildParams = buildParams,
        viewFactory = customisation.viewFactory(viewDependency),
        plugins = listOf(
            interactor,
            router,
            disposables(feature)
        )
    )
}
