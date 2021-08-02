package com.badoo.ribs.samples.routing.simple_routing.rib.child1

import com.badoo.ribs.core.Rib
import com.badoo.ribs.core.customisation.RibCustomisation

interface Child1 : Rib {

    interface Dependency {
        val title: String
    }

    class Customisation(
        val viewFactory: Child1View.Factory = Child1ViewImpl.Factory()
    ) : RibCustomisation
}
