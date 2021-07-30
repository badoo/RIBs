package com.badoo.ribs.samples.simplerouting.rib.simple_routing_child1_child2

import com.badoo.ribs.core.Rib
import com.badoo.ribs.core.customisation.RibCustomisation

interface SimpleRoutingChild1Child2 : Rib {

    interface Dependency

    class Customisation(
        val viewFactory: SimpleRoutingChild1Child2View.Factory = SimpleRoutingChild1Child2ViewImpl.Factory()
    ) : RibCustomisation
}
