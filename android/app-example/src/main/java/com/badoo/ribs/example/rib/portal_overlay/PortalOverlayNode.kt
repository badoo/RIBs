package com.badoo.ribs.example.rib.portal_overlay

import android.os.Bundle
import android.view.ViewGroup
import com.badoo.ribs.core.Node

class PortalOverlayNode(
    savedInstanceState: Bundle?,
    viewFactory: ((ViewGroup) -> PortalOverlayView?)?,
    router: PortalOverlayRouter,
    interactor: PortalOverlayInteractor
) : Node<PortalOverlayView>(
    savedInstanceState = savedInstanceState,
    identifier = object : PortalOverlay {},
    viewFactory = viewFactory,
    router = router,
    interactor = interactor
) {

}
