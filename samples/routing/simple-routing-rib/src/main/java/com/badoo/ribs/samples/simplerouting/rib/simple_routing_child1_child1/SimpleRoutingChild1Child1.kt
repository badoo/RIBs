package com.badoo.ribs.samples.simplerouting.rib.simple_routing_child1_child1

import com.badoo.ribs.core.Rib
import com.badoo.ribs.core.customisation.RibCustomisation

interface SimpleRoutingChild1Child1 : Rib {

    interface Dependency

    class Customisation(
        val viewFactory: SimpleRoutingChild1Child1View.Factory = SimpleRoutingChild1Child1ViewImpl.Factory()
    ) : RibCustomisation
}
