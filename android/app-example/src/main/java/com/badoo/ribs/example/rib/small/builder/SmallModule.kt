@file:SuppressWarnings("LongParameterList", "LongMethod")
package com.badoo.ribs.example.rib.small.builder

import android.os.Bundle
import com.badoo.ribs.core.routing.portal.Portal
import com.badoo.ribs.example.rib.big.builder.BigBuilder
import com.badoo.ribs.example.rib.portal_overlay_test.builder.PortalOverlayTestBuilder
import com.badoo.ribs.example.rib.small.Small
import com.badoo.ribs.example.rib.small.SmallInteractor
import com.badoo.ribs.example.rib.small.SmallNode
import com.badoo.ribs.example.rib.small.SmallRouter
import dagger.Provides

@dagger.Module
internal object SmallModule {

    @SmallScope
    @Provides
    @JvmStatic
    internal fun router(
        // pass component to child rib builders, or remove if there are none
        component: SmallComponent,
        savedInstanceState: Bundle?
    ): SmallRouter =
        SmallRouter(
            savedInstanceState = savedInstanceState,
            bigBuilder = BigBuilder(component),
            portalOverlayTestBuilder = PortalOverlayTestBuilder(component)
        )

    @SmallScope
    @Provides
    @JvmStatic
    internal fun interactor(
        savedInstanceState: Bundle?,
        router: SmallRouter,
        portal: Portal.OtherSide
    ): SmallInteractor =
        SmallInteractor(
            savedInstanceState = savedInstanceState,
            router = router,
            portal = portal
        )

    @SmallScope
    @Provides
    @JvmStatic
    internal fun node(
        savedInstanceState: Bundle?,
        customisation: Small.Customisation,
        router: SmallRouter,
        interactor: SmallInteractor
    ) : SmallNode = SmallNode(
        savedInstanceState = savedInstanceState,
        viewFactory = customisation.viewFactory(null),
        router = router,
        interactor = interactor
    )
}
