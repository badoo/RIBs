package com.badoo.ribs.example.rib.portal_overlay_test

import com.badoo.ribs.core.Rib
import com.badoo.ribs.customisation.CanProvideRibCustomisation
import com.badoo.ribs.customisation.RibCustomisation

interface PortalOverlayTest : Rib {

    interface Dependency : CanProvideRibCustomisation

    class Customisation(
        val viewFactory: PortalOverlayTestView.Factory = PortalOverlayTestViewImpl.Factory()
    ) : RibCustomisation
}
