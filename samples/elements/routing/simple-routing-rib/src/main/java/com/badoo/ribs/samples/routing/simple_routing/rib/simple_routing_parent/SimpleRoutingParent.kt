package com.badoo.ribs.samples.routing.simple_routing.rib.simple_routing_parent

import com.badoo.ribs.core.Rib
import com.badoo.ribs.core.customisation.RibCustomisation

interface SimpleRoutingParent : Rib {

    interface Dependency

    class Customisation(
        val viewFactory: SimpleRoutingParentView.Factory = SimpleRoutingParentViewImpl.Factory()
    ) : RibCustomisation
}
