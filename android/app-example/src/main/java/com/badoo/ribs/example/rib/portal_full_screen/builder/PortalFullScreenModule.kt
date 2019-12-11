@file:SuppressWarnings("LongParameterList", "LongMethod")
package com.badoo.ribs.example.rib.portal_full_screen.builder

import android.os.Bundle
import com.badoo.ribs.example.rib.portal_full_screen.PortalFullScreen
import com.badoo.ribs.example.rib.portal_full_screen.PortalFullScreenInteractor
import com.badoo.ribs.example.rib.portal_full_screen.PortalFullScreenNode
import com.badoo.ribs.example.rib.portal_full_screen.PortalFullScreenRouter
import com.badoo.ribs.example.rib.portal_sub_screen.builder.PortalSubScreenBuilder
import dagger.Provides

@dagger.Module
internal object PortalFullScreenModule {

    @PortalFullScreenScope
    @Provides
    @JvmStatic
    internal fun router(
        // pass component to child rib builders, or remove if there are none
        component: PortalFullScreenComponent,
        savedInstanceState: Bundle?
    ): PortalFullScreenRouter =
        PortalFullScreenRouter(
            savedInstanceState = savedInstanceState,
            smallBuilder = PortalSubScreenBuilder(component)
        )

    @PortalFullScreenScope
    @Provides
    @JvmStatic
    internal fun interactor(
        savedInstanceState: Bundle?,
        router: PortalFullScreenRouter
    ): PortalFullScreenInteractor =
        PortalFullScreenInteractor(
            savedInstanceState = savedInstanceState,
            router = router
        )

    @PortalFullScreenScope
    @Provides
    @JvmStatic
    internal fun node(
        savedInstanceState: Bundle?,
        customisation: PortalFullScreen.Customisation,
        router: PortalFullScreenRouter,
        interactor: PortalFullScreenInteractor
    ) : PortalFullScreenNode = PortalFullScreenNode(
        savedInstanceState = savedInstanceState,
        viewFactory = customisation.viewFactory(null),
        router = router,
        interactor = interactor
    )
}
