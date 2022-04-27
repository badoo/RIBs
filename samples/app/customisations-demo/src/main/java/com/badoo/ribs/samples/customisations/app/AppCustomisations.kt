package com.badoo.ribs.samples.customisations.app

import com.badoo.ribs.samples.routing.simple_routing.rib.child1.Child1
import com.badoo.ribs.samples.routing.simple_routing.rib.child1.Child1ViewImpl
import com.badoo.ribs.samples.routing.simple_routing.rib.child1_child1.Child1Child1
import com.badoo.ribs.samples.routing.simple_routing.rib.child1_child1.Child1Child1ViewImpl
import com.badoo.ribs.samples.routing.simple_routing.rib.child1_child2.Child1Child2
import com.badoo.ribs.samples.routing.simple_routing.rib.child1_child2.Child1Child2ViewImpl
import com.badoo.ribs.samples.routing.simple_routing.rib.child2.Child2
import com.badoo.ribs.samples.routing.simple_routing.rib.child2.Child2ViewImpl
import com.badoo.ribs.samples.routing.simple_routing.rib.simple_routing_parent.SimpleRoutingParent
import com.badoo.ribs.samples.routing.simple_routing.rib.simple_routing_parent.SimpleRoutingParentViewImpl
import com.bumble.appyx.utils.customisations.NodeCustomisationDirectoryImpl

object AppCustomisations : NodeCustomisationDirectoryImpl() {

    init {
        +SimpleRoutingParent.Customisation(
            viewFactory = SimpleRoutingParentViewImpl.Factory(R.layout.rib_customisation_parent)
        )
        SimpleRoutingParent::class {
            +Child1.Customisation(
                viewFactory = Child1ViewImpl.Factory(R.layout.rib_customisation_child1)
            )
            +Child2.Customisation(
                viewFactory = Child2ViewImpl.Factory(R.layout.rib_customisation_child2)
            )
            +Child1Child1.Customisation(
                viewFactory = Child1Child1ViewImpl.Factory(R.layout.rib_customisation_child1_child1)
            )
            +Child1Child2.Customisation(
                viewFactory = Child1Child2ViewImpl.Factory(R.layout.rib_customisation_child1_child2)
            )
        }
    }
}
