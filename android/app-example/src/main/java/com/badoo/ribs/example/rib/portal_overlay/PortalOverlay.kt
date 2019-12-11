package com.badoo.ribs.example.rib.portal_overlay

import com.badoo.ribs.core.Rib
import com.badoo.ribs.customisation.CanProvideRibCustomisation
import com.badoo.ribs.customisation.RibCustomisation

interface PortalOverlay : Rib {

    interface Dependency : CanProvideRibCustomisation

    class Customisation(
        val viewFactory: PortalOverlayView.Factory = PortalOverlayViewImpl.Factory()
    ) : RibCustomisation
}
