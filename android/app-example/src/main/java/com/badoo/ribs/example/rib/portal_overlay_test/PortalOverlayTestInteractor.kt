package com.badoo.ribs.example.rib.portal_overlay_test

import com.badoo.ribs.core.BuildParams
import com.badoo.ribs.core.Interactor
import com.badoo.ribs.core.Router
import com.badoo.ribs.example.rib.portal_overlay_test.PortalOverlayTestRouter.Configuration
import com.badoo.ribs.example.rib.portal_overlay_test.PortalOverlayTestRouter.Configuration.Content
import com.badoo.ribs.example.rib.portal_overlay_test.PortalOverlayTestRouter.Configuration.Overlay

class PortalOverlayTestInteractor(
    buildParams: BuildParams<Nothing?>,
    router: Router<Configuration, *, Content, Overlay, PortalOverlayTestView>
) : Interactor<Configuration, Content, Overlay, PortalOverlayTestView>(
    buildParams = buildParams,
    router = router,
    disposables = null
)
