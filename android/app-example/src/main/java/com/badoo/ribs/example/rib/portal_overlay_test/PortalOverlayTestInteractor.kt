package com.badoo.ribs.example.rib.portal_overlay_test

import android.os.Bundle
import com.badoo.ribs.core.Interactor
import com.badoo.ribs.core.Router
import com.badoo.ribs.example.rib.portal_overlay_test.PortalOverlayTestRouter.Configuration
import com.badoo.ribs.example.rib.portal_overlay_test.PortalOverlayTestRouter.Configuration.Content
import com.badoo.ribs.example.rib.portal_overlay_test.PortalOverlayTestRouter.Configuration.Overlay

class PortalOverlayTestInteractor(
    savedInstanceState: Bundle?,
    router: Router<Configuration, *, Content, Overlay, PortalOverlayTestView>
) : Interactor<Configuration, Content, Overlay, PortalOverlayTestView>(
    savedInstanceState = savedInstanceState,
    router = router,
    disposables = null
)
