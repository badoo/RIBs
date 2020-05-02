package com.badoo.ribs.example.rib.small

import com.badoo.ribs.core.Rib
import com.badoo.ribs.customisation.CanProvidePortal
import com.badoo.ribs.customisation.CanProvideRibCustomisation
import com.badoo.ribs.customisation.RibCustomisation

interface Small : Rib {

    interface Dependency :
        CanProvideRibCustomisation,
        CanProvidePortal

    class Customisation(
        val viewFactory: SmallView.Factory = SmallViewImpl.Factory()
    ) : RibCustomisation

    // Workflow
}
