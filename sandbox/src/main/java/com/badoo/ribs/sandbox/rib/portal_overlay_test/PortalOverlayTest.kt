package com.badoo.ribs.sandbox.rib.portal_overlay_test

import com.badoo.ribs.core.Rib
import com.bumble.appyx.utils.customisations.NodeCustomisation

interface PortalOverlayTest : Rib {

    interface Dependency

    class Customisation(
        val viewFactory: PortalOverlayTestView.Factory = PortalOverlayTestViewImpl.Factory()
    ) : NodeCustomisation
}
