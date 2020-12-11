@file:SuppressWarnings("LongParameterList", "LongMethod")
package com.badoo.ribs.template.node_dagger_build_param.foo_bar.builder

import com.badoo.ribs.core.builder.BuildParams
import com.badoo.ribs.template.node_dagger_build_param.foo_bar.FooBar
import com.badoo.ribs.template.node_dagger_build_param.foo_bar.FooBarInteractor
import com.badoo.ribs.template.node_dagger_build_param.foo_bar.FooBarNode
import com.badoo.ribs.template.node_dagger_build_param.foo_bar.FooBarRouter
import com.badoo.ribs.template.node_dagger_build_param.foo_bar.builder.FooBarBuilder.Params
import com.badoo.ribs.template.node_dagger_build_param.foo_bar.feature.FooBarFeature
import dagger.Provides

@dagger.Module
internal object FooBarModule {

    @FooBarScope
    @Provides
    @JvmStatic
    internal fun router(
        // pass component to child rib builders, or remove if there are none
        component: FooBarComponent,
        buildParams: BuildParams<Params>,
        customisation: FooBar.Customisation
    ): FooBarRouter =
        FooBarRouter(
            buildParams = buildParams,
            transitionHandler = null // Add customisation.transitionHandler if you need it
        )

    @FooBarScope
    @Provides
    @JvmStatic
    internal fun feature(): FooBarFeature =
        FooBarFeature()

    @FooBarScope
    @Provides
    @JvmStatic
    internal fun interactor(
        dependency: FooBar.Dependency,
        buildParams: BuildParams<Params>,
        router: FooBarRouter,
        feature: FooBarFeature
    ): FooBarInteractor =
        FooBarInteractor(
            buildParams = buildParams,
            router = router,
            input = dependency.fooBarInput,
            output = dependency.fooBarOutput,
            feature = feature
        )

    @FooBarScope
    @Provides
    @JvmStatic
    internal fun node(
        buildParams: BuildParams<Params>,
        customisation: FooBar.Customisation,
        router: FooBarRouter,
        interactor: FooBarInteractor,
        feature: FooBarFeature
    ) : FooBarNode = FooBarNode(
        buildParams = buildParams,
        viewFactory = customisation.viewFactory(null),
        router = router,
        interactor = interactor,
        feature = feature
    )
}
