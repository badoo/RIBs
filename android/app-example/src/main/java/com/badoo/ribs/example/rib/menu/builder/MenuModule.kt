@file:Suppress("LongParameterList")
package com.badoo.ribs.example.rib.menu.builder

import com.badoo.ribs.core.BuildContext
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
        buildContext: BuildContext.Resolved<Nothing?>
    ): MenuRouter =
        MenuRouter(
            buildContext = buildContext
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
        buildContext: BuildContext.Resolved<Nothing?>,
        router: MenuRouter,
        input: ObservableSource<Menu.Input>,
        output: Consumer<Menu.Output>,
        feature: MenuFeature
    ): MenuInteractor =
        MenuInteractor(
            buildContext = buildContext,
            router = router,
            input = input,
            output = output,
            feature = feature
        )

    @MenuScope
    @Provides
    @JvmStatic
    internal fun node(
        buildContext: BuildContext.Resolved<Nothing?>,
        customisation: Menu.Customisation,
        router: MenuRouter,
        interactor: MenuInteractor
    ) : Node<MenuView> = Node(
        buildContext = buildContext,
        viewFactory = customisation.viewFactory(null),
        router = router,
        interactor = interactor
    )
}
