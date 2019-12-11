@file:SuppressWarnings("LongParameterList", "LongMethod")
package com.badoo.ribs.example.rib.portal_sub_screen.builder

import android.os.Bundle
import com.badoo.ribs.core.routing.portal.Portal
import com.badoo.ribs.example.rib.portal_full_screen.builder.PortalFullScreenBuilder
import com.badoo.ribs.example.rib.portal_overlay.PortalOverlayBuilder
import com.badoo.ribs.example.rib.portal_sub_screen.PortalSubScreen
import com.badoo.ribs.example.rib.portal_sub_screen.PortalSubScreenInteractor
import com.badoo.ribs.example.rib.portal_sub_screen.PortalSubScreenNode
import com.badoo.ribs.example.rib.portal_sub_screen.PortalSubScreenRouter
import dagger.Provides

@dagger.Module
internal object PortalSubScreenModule {

    @PortalSubScreenScope
    @Provides
    @JvmStatic
    internal fun router(
        // pass component to child rib builders, or remove if there are none
        component: PortalSubScreenComponent,
        savedInstanceState: Bundle?
    ): PortalSubScreenRouter =
        PortalSubScreenRouter(
            savedInstanceState = savedInstanceState,
            bigBuilder = PortalFullScreenBuilder(component),
            portalOverlayTestBuilder = PortalOverlayBuilder(component)
        )

    @PortalSubScreenScope
    @Provides
    @JvmStatic
    internal fun interactor(
        savedInstanceState: Bundle?,
        router: PortalSubScreenRouter,
        portal: Portal.OtherSide
    ): PortalSubScreenInteractor =
        PortalSubScreenInteractor(
            savedInstanceState = savedInstanceState,
            router = router,
            portal = portal
        )

    @PortalSubScreenScope
    @Provides
    @JvmStatic
    internal fun node(
        savedInstanceState: Bundle?,
        customisation: PortalSubScreen.Customisation,
        router: PortalSubScreenRouter,
        interactor: PortalSubScreenInteractor
    ) : PortalSubScreenNode = PortalSubScreenNode(
        savedInstanceState = savedInstanceState,
        viewFactory = customisation.viewFactory(null),
        router = router,
        interactor = interactor
    )
}
