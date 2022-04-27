package com.badoo.ribs.samples.routing.simple_routing.rib.child2

import com.badoo.ribs.core.Rib
import com.bumble.appyx.utils.customisations.NodeCustomisation

interface Child2 : Rib {

    interface Dependency

    class Customisation(
        val viewFactory: Child2View.Factory = Child2ViewImpl.Factory()
    ) : NodeCustomisation
}
