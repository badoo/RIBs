package com.badoo.ribs.samples.transitionanimations.rib.child2

import com.badoo.ribs.core.Rib
import com.badoo.ribs.core.customisation.RibCustomisation

interface Child2 : Rib {

    interface Dependency

    class Customisation(
        val viewFactory: Child2View.Factory = Child2ViewImpl.Factory()
    ) : RibCustomisation
}
