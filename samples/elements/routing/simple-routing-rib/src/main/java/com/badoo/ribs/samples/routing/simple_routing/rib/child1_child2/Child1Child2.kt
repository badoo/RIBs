package com.badoo.ribs.samples.routing.simple_routing.rib.child1_child2

import com.badoo.ribs.core.Rib
import com.badoo.ribs.core.customisation.RibCustomisation

interface Child1Child2 : Rib {

    interface Dependency

    class Customisation(
        val viewFactory: Child1Child2View.Factory = Child1Child2ViewImpl.Factory()
    ) : RibCustomisation
}
