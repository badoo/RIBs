package com.badoo.ribs.samples.transitionanimations.rib.parent

import com.badoo.ribs.core.Rib
import com.badoo.ribs.core.customisation.RibCustomisation

interface Parent : Rib {

    interface Dependency

    class Customisation(
        val viewFactory: ParentView.Factory = ParentViewImpl.Factory()
    ) : RibCustomisation
}