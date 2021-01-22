package com.badoo.ribs.samples.simplerouting.simple_routing_child1

import com.badoo.ribs.core.Rib

interface SimpleRoutingChild1 : Rib {

    interface Dependency {
        val title: String
    }
}
