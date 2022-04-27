package com.badoo.ribs.samples.routing.simple_routing.rib.simple_routing_parent

import com.badoo.ribs.core.Rib
import com.bumble.appyx.utils.customisations.NodeCustomisation

interface SimpleRoutingParent : Rib {

    interface Dependency

    class Customisation(
        val viewFactory: SimpleRoutingParentView.Factory = SimpleRoutingParentViewImpl.Factory()
    ) : NodeCustomisation
}
