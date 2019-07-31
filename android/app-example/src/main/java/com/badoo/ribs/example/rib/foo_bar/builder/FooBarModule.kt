package com.badoo.ribs.example.rib.foo_bar.builder

import android.os.Bundle
import com.badoo.ribs.android.PermissionRequester
import com.badoo.ribs.core.Node
import com.badoo.ribs.example.rib.foo_bar.FooBar
import com.badoo.ribs.example.rib.foo_bar.FooBarInteractor
import com.badoo.ribs.example.rib.foo_bar.FooBarRouter
import com.badoo.ribs.example.rib.foo_bar.FooBarView
import com.badoo.ribs.example.rib.foo_bar.feature.FooBarFeature
import dagger.Provides

@dagger.Module
internal object FooBarModule {

    @FooBarScope
    @Provides
    @JvmStatic
    internal fun router(
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
        permissionRequester: PermissionRequester
    ): FooBarInteractor =
        FooBarInteractor(
            savedInstanceState = savedInstanceState,
            router = router,
            permissionRequester = permissionRequester
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
