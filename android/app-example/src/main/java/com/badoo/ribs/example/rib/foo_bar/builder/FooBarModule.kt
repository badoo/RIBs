@file:SuppressWarnings("LongParameterList")
package com.badoo.ribs.example.rib.foo_bar.builder

import com.badoo.ribs.core.builder.BuildParams
import com.badoo.ribs.android.PermissionRequester
import com.badoo.ribs.core.view.ViewPlugin
import com.badoo.ribs.example.rib.foo_bar.FooBar
import com.badoo.ribs.example.rib.foo_bar.FooBarInteractor
import com.badoo.ribs.example.rib.foo_bar.FooBarNode
import com.badoo.ribs.example.rib.foo_bar.FooBarRouter
import com.badoo.ribs.example.rib.foo_bar.feature.FooBarFeature
import com.badoo.ribs.example.rib.foo_bar.viewplugin.ParentLongClickListener
import dagger.Provides

@dagger.Module
internal object FooBarModule {

    @FooBarScope
    @Provides
    @JvmStatic
    internal fun router(
        buildParams: BuildParams<Nothing?>
    ): FooBarRouter =
        FooBarRouter(
            buildParams = buildParams
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
        buildParams: BuildParams<Nothing?>,
        router: FooBarRouter,
        permissionRequester: PermissionRequester
    ): FooBarInteractor =
        FooBarInteractor(
            buildParams = buildParams,
            router = router,
            permissionRequester = permissionRequester
        )

    @FooBarScope
    @Provides
    @JvmStatic
    internal fun viewPlugins(): Set<@JvmSuppressWildcards ViewPlugin> = setOf(
        ParentLongClickListener()
    )

    @FooBarScope
    @Provides
    @JvmStatic
    internal fun node(
        buildParams: BuildParams<Nothing?>,
        customisation: FooBar.Customisation,
        router: FooBarRouter,
        interactor: FooBarInteractor,
        viewPlugins: Set<@JvmSuppressWildcards ViewPlugin>
    ) : FooBarNode = FooBarNode(
        buildParams = buildParams,
        viewFactory = customisation.viewFactory(null),
        router = router,
        interactor = interactor,
        viewPlugins = viewPlugins
    )
}
