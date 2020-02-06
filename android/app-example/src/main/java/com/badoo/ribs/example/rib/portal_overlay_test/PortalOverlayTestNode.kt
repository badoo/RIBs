package com.badoo.ribs.example.rib.portal_overlay_test

import com.badoo.ribs.core.BuildParams
import android.view.ViewGroup
import com.badoo.ribs.core.Node

class PortalOverlayTestNode(
    buildParams: BuildParams<*>,
    viewFactory: ((ViewGroup) -> PortalOverlayTestView?)?,
    router: PortalOverlayTestRouter,
    interactor: PortalOverlayTestInteractor
) : Node<PortalOverlayTestView>(
    buildParams = buildParams,
    viewFactory = viewFactory,
    router = router,
    interactor = interactor
) {

}
