package com.badoo.ribs.example.rib.portal_full_screen

import com.badoo.ribs.core.Rib
import com.badoo.ribs.customisation.CanProvidePortal
import com.badoo.ribs.customisation.CanProvideRibCustomisation
import com.badoo.ribs.customisation.RibCustomisation

interface PortalFullScreen : Rib {

    interface Dependency :
        CanProvideRibCustomisation,
        CanProvidePortal

    class Customisation(
        val viewFactory: PortalFullScreenView.Factory = PortalFullScreenViewImpl.Factory()
    ) : RibCustomisation

    interface Workflow
}
