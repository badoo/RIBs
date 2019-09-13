@file:SuppressWarnings("LongParameterList", "LongMethod")
package com.badoo.ribs.example.rib.portal_overlay_test.builder

import android.os.Bundle
import com.badoo.ribs.example.rib.portal_overlay_test.PortalOverlayTest
import com.badoo.ribs.example.rib.portal_overlay_test.PortalOverlayTestInteractor
import com.badoo.ribs.example.rib.portal_overlay_test.PortalOverlayTestNode
import com.badoo.ribs.example.rib.portal_overlay_test.PortalOverlayTestRouter
import dagger.Provides

@dagger.Module
internal object PortalOverlayTestModule {

    @PortalOverlayTestScope
    @Provides
    @JvmStatic
    internal fun router(
        // pass component to child rib builders, or remove if there are none
        component: PortalOverlayTestComponent,
        savedInstanceState: Bundle?
    ): PortalOverlayTestRouter =
        PortalOverlayTestRouter(
            savedInstanceState = savedInstanceState
        )

    @PortalOverlayTestScope
    @Provides
    @JvmStatic
    internal fun interactor(
        savedInstanceState: Bundle?,
        router: PortalOverlayTestRouter
    ): PortalOverlayTestInteractor =
        PortalOverlayTestInteractor(
            savedInstanceState = savedInstanceState,
            router = router
        )

    @PortalOverlayTestScope
    @Provides
    @JvmStatic
    internal fun node(
        savedInstanceState: Bundle?,
        customisation: PortalOverlayTest.Customisation,
        router: PortalOverlayTestRouter,
        interactor: PortalOverlayTestInteractor
    ) : PortalOverlayTestNode = PortalOverlayTestNode(
        savedInstanceState = savedInstanceState,
        viewFactory = customisation.viewFactory(null),
        router = router,
        interactor = interactor
    )
}
