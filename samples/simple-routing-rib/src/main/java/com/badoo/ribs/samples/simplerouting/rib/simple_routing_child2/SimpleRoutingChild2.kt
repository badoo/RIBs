package com.badoo.ribs.samples.simplerouting.rib.simple_routing_child2

import com.badoo.ribs.core.Rib
import com.badoo.ribs.core.customisation.RibCustomisation

interface SimpleRoutingChild2 : Rib {

    interface Dependency

    class Customisation(
            val viewFactory: SimpleRoutingChild2View.Factory = SimpleRoutingChild2ViewImpl.Factory()
    ) : RibCustomisation
}
