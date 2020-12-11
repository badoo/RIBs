@file:SuppressWarnings("LongParameterList", "LongMethod")
package com.badoo.ribs.template.node_dagger.foo_bar.builder

import com.badoo.ribs.core.builder.BuildParams
import com.badoo.ribs.template.node_dagger.foo_bar.FooBar
import com.badoo.ribs.template.node_dagger.foo_bar.FooBarInteractor
import com.badoo.ribs.template.node_dagger.foo_bar.FooBarNode
import com.badoo.ribs.template.node_dagger.foo_bar.FooBarRouter
import com.badoo.ribs.template.node_dagger.foo_bar.feature.FooBarFeature
import dagger.Provides

@dagger.Module
internal object FooBarModule {

    @FooBarScope
    @Provides
    @JvmStatic
    internal fun router(
        // pass component to child rib builders, or remove if there are none
        component: FooBarComponent,
        buildParams: BuildParams<Nothing?>,
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
        buildParams: BuildParams<Nothing?>,
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
        buildParams: BuildParams<Nothing?>,
        customisation: FooBar.Customisation,
        interactor: FooBarInteractor,
        router: FooBarRouter
    ) : FooBarNode = FooBarNode(
        buildParams = buildParams,
        viewFactory = customisation.viewFactory(null),
        plugins = listOf(interactor, router)
    )
}
