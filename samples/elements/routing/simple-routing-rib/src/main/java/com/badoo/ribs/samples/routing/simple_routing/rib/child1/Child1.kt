package com.badoo.ribs.samples.routing.simple_routing.rib.child1

import com.badoo.ribs.core.Rib
import com.bumble.appyx.utils.customisations.NodeCustomisation

interface Child1 : Rib {

    interface Dependency {
        val title: String
    }

    class Customisation(
        val viewFactory: Child1View.Factory = Child1ViewImpl.Factory()
    ) : NodeCustomisation
}
