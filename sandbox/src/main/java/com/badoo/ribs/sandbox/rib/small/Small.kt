package com.badoo.ribs.sandbox.rib.small

import com.badoo.ribs.core.Rib
import com.badoo.ribs.portal.CanProvidePortal
import com.badoo.ribs.customisation.RibCustomisation

interface Small : Rib {

    interface Dependency :
        CanProvidePortal

    class Customisation(
        val viewFactory: SmallView.Factory = SmallViewImpl.Factory()
    ) : RibCustomisation

    // Workflow
}
