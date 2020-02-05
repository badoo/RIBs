@file:SuppressWarnings("LongParameterList", "LongMethod")
package com.badoo.ribs.example.rib.portal_overlay_test.builder

import com.badoo.ribs.core.BuildContext
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
        buildContext: BuildContext<Nothing?>
    ): PortalOverlayTestRouter =
        PortalOverlayTestRouter(
            buildContext = buildContext
        )

    @PortalOverlayTestScope
    @Provides
    @JvmStatic
    internal fun interactor(
        buildContext: BuildContext<Nothing?>,
        router: PortalOverlayTestRouter
    ): PortalOverlayTestInteractor =
        PortalOverlayTestInteractor(
            buildContext = buildContext,
            router = router
        )

    @PortalOverlayTestScope
    @Provides
    @JvmStatic
    internal fun node(
        buildContext: BuildContext<Nothing?>,
        customisation: PortalOverlayTest.Customisation,
        router: PortalOverlayTestRouter,
        interactor: PortalOverlayTestInteractor
    ) : PortalOverlayTestNode = PortalOverlayTestNode(
        buildContext = buildContext,
        viewFactory = customisation.viewFactory(null),
        router = router,
        interactor = interactor
    )
}
