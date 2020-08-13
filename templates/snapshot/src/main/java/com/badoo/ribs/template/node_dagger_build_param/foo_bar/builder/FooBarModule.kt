@file:SuppressWarnings("LongParameterList", "LongMethod")
package com.badoo.ribs.template.node_dagger_build_param.foo_bar.builder

import com.badoo.ribs.core.modality.BuildParams
import com.badoo.ribs.routing.source.backstack.BackStackFeature
import com.badoo.ribs.rx.disposeOnDetach
import com.badoo.ribs.template.node_dagger_build_param.foo_bar.FooBar
import com.badoo.ribs.template.node_dagger_build_param.foo_bar.FooBarInteractor
import com.badoo.ribs.template.node_dagger_build_param.foo_bar.FooBarNode
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
    @JvmStatic
    internal fun feature(): FooBarFeature =
        FooBarFeature()

    @FooBarScope
    @Provides
    @JvmStatic
    internal fun backStack(
        buildParams: BuildParams<Params>
    ): BackStackFeature<Configuration> =
        BackStackFeature(
            buildParams = buildParams,
            initialConfiguration = Content.Default
        )

    @FooBarScope
    @Provides
    @JvmStatic
    internal fun interactor(
        dependency: FooBar.Dependency,
        buildParams: BuildParams<Params>,
        backStack: BackStackFeature<Configuration>,
        feature: FooBarFeature
    ): FooBarInteractor =
        FooBarInteractor(
            buildParams = buildParams,
            backStack= backStack,
            feature = feature
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
        buildParams: BuildParams<Params>,
        backStack: BackStackFeature<Configuration>,
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
    @JvmStatic
    internal fun node(
        buildParams: BuildParams<Params>,
        customisation: FooBar.Customisation,
        feature: FooBarFeature,
        interactor: FooBarInteractor,
        router: FooBarRouter
    ) : FooBarNode = FooBarNode(
        buildParams = buildParams,
        viewFactory = customisation.viewFactory(null),
        plugins = listOf(interactor, router, disposeOnDetach(feature))
    )
}
