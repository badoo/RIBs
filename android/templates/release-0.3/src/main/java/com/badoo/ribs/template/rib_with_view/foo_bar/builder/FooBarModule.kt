@file:Suppress("LongParameterList")
package com.badoo.ribs.template.rib_with_view.foo_bar.builder

import android.os.Bundle
import com.badoo.ribs.core.Node
import com.badoo.ribs.template.rib_with_view.foo_bar.FooBar
import com.badoo.ribs.template.rib_with_view.foo_bar.FooBar.Input
import com.badoo.ribs.template.rib_with_view.foo_bar.FooBar.Output
import com.badoo.ribs.template.rib_with_view.foo_bar.FooBarInteractor
import com.badoo.ribs.template.rib_with_view.foo_bar.FooBarRouter
import com.badoo.ribs.template.rib_with_view.foo_bar.FooBarView
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
        savedInstanceState: Bundle?
    ): FooBarRouter =
        FooBarRouter(
            savedInstanceState = savedInstanceState
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
        router: FooBarRouter,
        input: ObservableSource<Input>,
        output: Consumer<Output>,
        feature: FooBarFeature
    ): FooBarInteractor =
        FooBarInteractor(
            savedInstanceState = savedInstanceState,
            router = router,
            input = input,
            output = output,
            feature = feature
        )

    @FooBarScope
    @Provides
    @JvmStatic
    internal fun node(
        savedInstanceState: Bundle?,
        customisation: FooBar.Customisation,
        router: FooBarRouter,
        interactor: FooBarInteractor
    ) : Node<FooBarView> = Node(
        savedInstanceState = savedInstanceState,
        identifier = object : FooBar {},
        viewFactory = customisation.viewFactory(null),
        router = router,
        interactor = interactor
    )
}
