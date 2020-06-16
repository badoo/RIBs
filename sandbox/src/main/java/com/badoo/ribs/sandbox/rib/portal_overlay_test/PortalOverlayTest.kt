package com.badoo.ribs.sandbox.rib.portal_overlay_test

import com.badoo.ribs.core.Rib
import com.badoo.ribs.core.customisation.RibCustomisation

interface PortalOverlayTest : Rib {

}

class Customisation(
    val viewFactory: PortalOverlayTestView.Factory = PortalOverlayTestViewImpl.Factory()
) : RibCustomisation

interface Dependency
