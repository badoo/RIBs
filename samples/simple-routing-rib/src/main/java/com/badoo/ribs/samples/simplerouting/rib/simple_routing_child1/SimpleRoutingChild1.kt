package com.badoo.ribs.samples.simplerouting.rib.simple_routing_child1

import com.badoo.ribs.core.Rib
import com.badoo.ribs.core.customisation.RibCustomisation

interface SimpleRoutingChild1 : Rib {

    interface Dependency {
        val title: String
    }

    class Customisation(
            val viewFactory: SimpleRoutingChild1View.Factory = SimpleRoutingChild1ViewImpl.Factory()
    ) : RibCustomisation
}
