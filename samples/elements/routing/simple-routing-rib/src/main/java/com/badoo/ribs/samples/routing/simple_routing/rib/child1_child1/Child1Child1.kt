package com.badoo.ribs.samples.routing.simple_routing.rib.child1_child1

import com.badoo.ribs.core.Rib
import com.bumble.appyx.utils.customisations.NodeCustomisation

interface Child1Child1 : Rib {

    interface Dependency

    class Customisation(
        val viewFactory: Child1Child1View.Factory = Child1Child1ViewImpl.Factory()
    ) : NodeCustomisation
}
