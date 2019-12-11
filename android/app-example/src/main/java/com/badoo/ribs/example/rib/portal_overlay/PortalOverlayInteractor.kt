package com.badoo.ribs.example.rib.portal_overlay

import android.os.Bundle
import com.badoo.ribs.core.Interactor
import com.badoo.ribs.core.Router
import com.badoo.ribs.example.rib.portal_overlay.PortalOverlayRouter.Configuration
import com.badoo.ribs.example.rib.portal_overlay.PortalOverlayRouter.Configuration.Content
import com.badoo.ribs.example.rib.portal_overlay.PortalOverlayRouter.Configuration.Overlay

class PortalOverlayInteractor(
    savedInstanceState: Bundle?,
    router: Router<Configuration, *, Content, Overlay, PortalOverlayView>
) : Interactor<Configuration, Content, Overlay, PortalOverlayView>(
    savedInstanceState = savedInstanceState,
    router = router,
    disposables = null
)
