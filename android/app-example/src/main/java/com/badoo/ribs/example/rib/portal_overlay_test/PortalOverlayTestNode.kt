package com.badoo.ribs.example.rib.portal_overlay_test

import com.badoo.ribs.core.BuildContext
import android.view.ViewGroup
import com.badoo.ribs.core.Node

class PortalOverlayTestNode(
    buildContext: BuildContext.Resolved<*>,
    viewFactory: ((ViewGroup) -> PortalOverlayTestView?)?,
    router: PortalOverlayTestRouter,
    interactor: PortalOverlayTestInteractor
) : Node<PortalOverlayTestView>(
    buildContext = buildContext,
    viewFactory = viewFactory,
    router = router,
    interactor = interactor
) {

}
