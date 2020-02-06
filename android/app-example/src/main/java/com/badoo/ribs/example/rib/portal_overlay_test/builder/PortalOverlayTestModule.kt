@file:SuppressWarnings("LongParameterList", "LongMethod")
package com.badoo.ribs.example.rib.portal_overlay_test.builder

import com.badoo.ribs.core.builder.BuildParams
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
        buildParams: BuildParams<Nothing?>
    ): PortalOverlayTestRouter =
        PortalOverlayTestRouter(
            buildParams = buildParams
        )

    @PortalOverlayTestScope
    @Provides
    @JvmStatic
    internal fun interactor(
        buildParams: BuildParams<Nothing?>,
        router: PortalOverlayTestRouter
    ): PortalOverlayTestInteractor =
        PortalOverlayTestInteractor(
            buildParams = buildParams,
            router = router
        )

    @PortalOverlayTestScope
    @Provides
    @JvmStatic
    internal fun node(
        buildParams: BuildParams<Nothing?>,
        customisation: PortalOverlayTest.Customisation,
        router: PortalOverlayTestRouter,
        interactor: PortalOverlayTestInteractor
    ) : PortalOverlayTestNode = PortalOverlayTestNode(
        buildParams = buildParams,
        viewFactory = customisation.viewFactory(null),
        router = router,
        interactor = interactor
    )
}
