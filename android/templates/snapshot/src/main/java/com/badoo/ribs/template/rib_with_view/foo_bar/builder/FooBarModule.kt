@file:SuppressWarnings("LongParameterList", "LongMethod")
package com.badoo.ribs.template.rib_with_view.foo_bar.builder

import com.badoo.ribs.core.BuildContext
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
        buildContext: BuildContext.Resolved<Nothing?>
    ): FooBarRouter =
        FooBarRouter(
            buildContext = buildContext
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
        buildContext: BuildContext.Resolved<Nothing?>,
        router: FooBarRouter,
        input: ObservableSource<Input>,
        output: Consumer<Output>,
        feature: FooBarFeature
    ): FooBarInteractor =
        FooBarInteractor(
            buildContext = buildContext,
            router = router,
            input = input,
            output = output,
            feature = feature
        )

    @FooBarScope
    @Provides
    @JvmStatic
    internal fun node(
        buildContext: BuildContext.Resolved<Nothing?>,
        customisation: FooBar.Customisation,
        router: FooBarRouter,
        interactor: FooBarInteractor,
        input: ObservableSource<Input>,
        output: Consumer<Output>,
        feature: FooBarFeature
    ) : FooBarNode = FooBarNode(
        buildContext = buildContext,
        viewFactory = customisation.viewFactory(null),
        router = router,
        interactor = interactor,
        input = input,
        output = output,
        feature = feature
    )
}
