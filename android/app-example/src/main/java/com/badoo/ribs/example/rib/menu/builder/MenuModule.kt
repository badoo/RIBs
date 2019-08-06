@file:Suppress("LongParameterList")
package com.badoo.ribs.example.rib.menu.builder

import android.os.Bundle
import com.badoo.ribs.core.Node
import com.badoo.ribs.example.rib.menu.Menu
import com.badoo.ribs.example.rib.menu.MenuInteractor
import com.badoo.ribs.example.rib.menu.MenuRouter
import com.badoo.ribs.example.rib.menu.MenuView
import com.badoo.ribs.example.rib.menu.feature.MenuFeature
import dagger.Provides
import io.reactivex.ObservableSource
import io.reactivex.functions.Consumer

@dagger.Module
internal object MenuModule {

    @MenuScope
    @Provides
    @JvmStatic
    internal fun router(
        savedInstanceState: Bundle?
    ): MenuRouter =
        MenuRouter(
            savedInstanceState = savedInstanceState
        )

    @MenuScope
    @Provides
    @JvmStatic
    fun feature(): MenuFeature =
        MenuFeature()

    @MenuScope
    @Provides
    @JvmStatic
    fun interactor(
        savedInstanceState: Bundle?,
        router: MenuRouter,
        input: ObservableSource<Menu.Input>,
        output: Consumer<Menu.Output>,
        feature: MenuFeature
    ): MenuInteractor =
        MenuInteractor(
            savedInstanceState = savedInstanceState,
            router = router,
            input = input,
            output = output,
            feature = feature
        )

    @MenuScope
    @Provides
    @JvmStatic
    internal fun node(
        savedInstanceState: Bundle?,
        customisation: Menu.Customisation,
        router: MenuRouter,
        interactor: MenuInteractor
    ) : Node<MenuView> = Node(
        savedInstanceState = savedInstanceState,
        identifier = object : Menu {},
        viewFactory = customisation.viewFactory(null),
        router = router,
        interactor = interactor
    )
}
