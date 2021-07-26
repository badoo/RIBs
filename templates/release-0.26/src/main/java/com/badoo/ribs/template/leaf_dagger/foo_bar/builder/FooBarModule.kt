@file:SuppressWarnings("LongParameterList", "LongMethod")

package com.badoo.ribs.template.leaf_dagger.foo_bar.builder

import com.badoo.ribs.core.modality.BuildParams
import com.badoo.ribs.rx.disposables
import com.badoo.ribs.template.leaf_dagger.foo_bar.FooBar
import com.badoo.ribs.template.leaf_dagger.foo_bar.FooBarInteractor
import com.badoo.ribs.template.leaf_dagger.foo_bar.FooBarNode
import com.badoo.ribs.template.leaf_dagger.foo_bar.FooBarView
import com.badoo.ribs.template.leaf_dagger.foo_bar.feature.FooBarFeature
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
    internal fun interactor(
        buildParams: BuildParams<Nothing?>,
        feature: FooBarFeature,
    ): FooBarInteractor =
        FooBarInteractor(
            buildParams = buildParams,
            feature = feature,
        )

    @FooBarScope
    @Provides
    @JvmStatic
    internal fun viewDependency(): FooBarView.ViewDependency =
        object : FooBarView.ViewDependency {

        }

    @FooBarScope
    @Provides
    @JvmStatic
    internal fun node(
        buildParams: BuildParams<Nothing?>,
        customisation: FooBar.Customisation,
        interactor: FooBarInteractor,
        feature: FooBarFeature,
        viewDependency: FooBarView.ViewDependency,
    ): FooBarNode =
        FooBarNode(
            buildParams = buildParams,
            viewFactory = customisation.viewFactory(viewDependency),
            plugins = listOf(
                interactor,
                disposables(feature)
            )
        )
}
