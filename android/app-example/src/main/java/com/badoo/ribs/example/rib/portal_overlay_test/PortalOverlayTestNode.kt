package com.badoo.ribs.example.rib.portal_overlay_test

import android.os.Bundle
import android.view.ViewGroup
import com.badoo.ribs.core.Node

class PortalOverlayTestNode(
    savedInstanceState: Bundle?,
    viewFactory: ((ViewGroup) -> PortalOverlayTestView?)?,
    interactor: PortalOverlayTestInteractor
) : Node<PortalOverlayTestView>(
    savedInstanceState = savedInstanceState,
    identifier = object : PortalOverlayTest {},
    viewFactory = viewFactory,
    router = null,
    interactor = interactor
) {

}
