package com.badoo.ribs.example.rib.foo_bar.builder

import com.badoo.ribs.android.PermissionRequester
import com.badoo.ribs.base.leaf.LeafNode
import com.badoo.ribs.core.Node
import com.badoo.ribs.core.view.ViewFactory
import com.badoo.ribs.example.rib.foo_bar.FooBar
import com.badoo.ribs.example.rib.foo_bar.FooBarInteractor
import com.badoo.ribs.example.rib.foo_bar.FooBarView
import com.badoo.ribs.example.rib.foo_bar.feature.FooBarFeature
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
        permissionRequester: PermissionRequester
    ): FooBarInteractor =
        FooBarInteractor(
            permissionRequester = permissionRequester
        )

    @FooBarScope
    @Provides
    @JvmStatic
    internal fun node(
        viewFactory: ViewFactory<FooBarView>,
        interactor: FooBarInteractor
    ) : Node<FooBarView> = LeafNode(
        identifier = object : FooBar {},
        viewFactory = viewFactory,
        interactor = interactor
    )
}
