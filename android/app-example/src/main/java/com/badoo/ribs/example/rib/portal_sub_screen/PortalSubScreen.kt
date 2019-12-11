package com.badoo.ribs.example.rib.portal_sub_screen

import com.badoo.ribs.core.Rib
import com.badoo.ribs.customisation.CanProvidePortal
import com.badoo.ribs.customisation.CanProvideRibCustomisation
import com.badoo.ribs.customisation.RibCustomisation

interface PortalSubScreen : Rib {

    interface Dependency :
        CanProvideRibCustomisation,
        CanProvidePortal

    class Customisation(
        val viewFactory: PortalSubScreenView.Factory = PortalSubScreenViewImpl.Factory()
    ) : RibCustomisation

    interface Workflow
}
