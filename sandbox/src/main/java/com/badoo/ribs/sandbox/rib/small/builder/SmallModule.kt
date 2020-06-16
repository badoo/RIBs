@file:SuppressWarnings("LongParameterList", "LongMethod")
package com.badoo.ribs.sandbox.rib.small.builder

import com.badoo.ribs.core.modality.BuildParams
import com.badoo.ribs.portal.Portal
import com.badoo.ribs.sandbox.rib.big.builder.BigBuilder
import com.badoo.ribs.sandbox.rib.portal_overlay_test.PortalOverlayTestBuilder
import com.badoo.ribs.sandbox.rib.small.Small
import com.badoo.ribs.sandbox.rib.small.SmallInteractor
import com.badoo.ribs.sandbox.rib.small.SmallNode
import com.badoo.ribs.sandbox.rib.small.SmallRouter
import dagger.Provides

@dagger.Module
internal object SmallModule {

    @SmallScope
    @Provides
    @JvmStatic
    internal fun interactor(
        buildParams: BuildParams<Nothing?>,
        portal: Portal.OtherSide
    ): SmallInteractor =
        SmallInteractor(
            buildParams = buildParams,
            portal = portal
        )

    @SmallScope
    @Provides
    @JvmStatic
    internal fun router(
        // pass component to child rib builders, or remove if there are none
        component: SmallComponent,
        buildParams: BuildParams<Nothing?>,
        interactor: SmallInteractor
    ): SmallRouter =
        SmallRouter(
            buildParams = buildParams,
            routingSource = interactor,
            bigBuilder = BigBuilder(),
            portalOverlayTestBuilder = PortalOverlayTestBuilder(TODO())
        )

    @SmallScope
    @Provides
    @JvmStatic
    internal fun node(
        buildParams: BuildParams<Nothing?>,
        customisation: Small.Customisation,
        router: SmallRouter,
        interactor: SmallInteractor
    ) : SmallNode = SmallNode(
        buildParams = buildParams,
        viewFactory = customisation.viewFactory(null),
        plugins =  listOf(
            interactor,
            router
        )
    )
}
