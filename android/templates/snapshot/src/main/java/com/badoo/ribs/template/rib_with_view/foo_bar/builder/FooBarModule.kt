@file:SuppressWarnings("LongParameterList", "LongMethod")
package com.badoo.ribs.template.rib_with_view.foo_bar.builder

import android.os.Bundle
import com.badoo.ribs.template.rib_with_view.foo_bar.FooBar
import com.badoo.ribs.template.rib_with_view.foo_bar.FooBar.Input
import com.badoo.ribs.template.rib_with_view.foo_bar.FooBar.Output
import com.badoo.ribs.template.rib_with_view.foo_bar.FooBarInteractor
import com.badoo.ribs.template.rib_with_view.foo_bar.FooBarNode
import com.badoo.ribs.template.rib_with_view.foo_bar.FooBarRouter
import com.badoo.ribs.template.rib_with_view.foo_bar.feature.FooBarFeature
import dagger.Provides
import io.reactivex.ObservableSource
import io.reactivex.functions.Consumer

@dagger.Module
internal object FooBarModule {

    @FooBarScope
    @Provides
    @JvmStatic
    internal fun router(
        // pass component to child rib builders, or remove if there are none
        component: FooBarComponent,
        savedInstanceState: Bundle?,
        customisation: FooBar.Customisation
    ): FooBarRouter =
        FooBarRouter(
            savedInstanceState = savedInstanceState,
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
        savedInstanceState: Bundle?,
        dependency: FooBar.Dependency,
        router: FooBarRouter,
        feature: FooBarFeature
    ): FooBarInteractor =
        FooBarInteractor(
            savedInstanceState = savedInstanceState,
            router = router,
            input = dependency.fooBarInput(),
            output = dependency.fooBarOutput(),
            feature = feature
        )

    @FooBarScope
    @Provides
    @JvmStatic
    internal fun node(
        savedInstanceState: Bundle?,
        customisation: FooBar.Customisation,
        router: FooBarRouter,
        interactor: FooBarInteractor,
        dependency: FooBar.Dependency,
        feature: FooBarFeature
    ) : FooBarNode = FooBarNode(
        savedInstanceState = savedInstanceState,
        viewFactory = customisation.viewFactory(null),
        router = router,
        interactor = interactor,
        input = dependency.fooBarInput(),
        output = dependency.fooBarOutput(),
        feature = feature
    )
}
