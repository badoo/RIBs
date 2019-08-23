@file:SuppressWarnings("LongParameterList", "LongMethod")
package com.badoo.ribs.example.rib.root.builder

import android.os.Bundle
import com.badoo.ribs.core.Portal
import com.badoo.ribs.example.rib.root.Root
import com.badoo.ribs.example.rib.root.RootInteractor
import com.badoo.ribs.example.rib.root.RootNode
import com.badoo.ribs.example.rib.root.RootRouter
import com.badoo.ribs.example.rib.switcher.builder.SwitcherBuilder
import com.badoo.ribs.example.rib.switcher.feature.PortalFeature
import com.badoo.ribs.example.util.CoffeeMachine
import com.badoo.ribs.example.util.StupidCoffeeMachine
import dagger.Provides
import javax.inject.Provider

@dagger.Module
internal object RootModule {

    @RootScope
    @Provides
    @JvmStatic
    internal fun portalFeature(
        node: Provider<RootNode>
    ): PortalFeature =
        PortalFeature(node)

    @RootScope
    @Provides
    @JvmStatic
    internal fun portal(
        portalFeature: PortalFeature
    ): Portal.Sink =
        portalFeature

    @RootScope
    @Provides
    @JvmStatic
    internal fun router(
        // pass component to child rib builders, or remove if there are none
        component: RootComponent,
        savedInstanceState: Bundle?
    ): RootRouter =
        RootRouter(
            savedInstanceState = savedInstanceState,
            switcherBuilder = SwitcherBuilder(component)
        )

    @RootScope
    @Provides
    @JvmStatic
    internal fun interactor(
        savedInstanceState: Bundle?,
        router: RootRouter,
        portalFeature: PortalFeature
    ): RootInteractor =
        RootInteractor(
            savedInstanceState = savedInstanceState,
            router = router,
            portal = portalFeature
        )

    @RootScope
    @Provides
    @JvmStatic
    internal fun node(
        savedInstanceState: Bundle?,
        customisation: Root.Customisation,
        router: RootRouter,
        interactor: RootInteractor
    ) : RootNode = RootNode(
        savedInstanceState = savedInstanceState,
        viewFactory = customisation.viewFactory(null),
        router = router,
        interactor = interactor
    )

    @RootScope
    @Provides
    @JvmStatic
    internal fun coffeeMachine(): CoffeeMachine =
        StupidCoffeeMachine()
}
