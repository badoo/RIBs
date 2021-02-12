package com.badoo.ribs.samples.customisations.app

import com.badoo.ribs.core.customisation.RibCustomisationDirectoryImpl
import com.badoo.ribs.samples.simplerouting.rib.simple_routing_child1.SimpleRoutingChild1
import com.badoo.ribs.samples.simplerouting.rib.simple_routing_child1.SimpleRoutingChild1ViewImpl
import com.badoo.ribs.samples.simplerouting.rib.simple_routing_child1_child1.SimpleRoutingChild1Child1
import com.badoo.ribs.samples.simplerouting.rib.simple_routing_child1_child1.SimpleRoutingChild1Child1ViewImpl
import com.badoo.ribs.samples.simplerouting.rib.simple_routing_child1_child2.SimpleRoutingChild1Child2
import com.badoo.ribs.samples.simplerouting.rib.simple_routing_child1_child2.SimpleRoutingChild1Child2ViewImpl
import com.badoo.ribs.samples.simplerouting.rib.simple_routing_child2.SimpleRoutingChild2
import com.badoo.ribs.samples.simplerouting.rib.simple_routing_child2.SimpleRoutingChild2ViewImpl
import com.badoo.ribs.samples.simplerouting.rib.simple_routing_parent.SimpleRoutingParent
import com.badoo.ribs.samples.simplerouting.rib.simple_routing_parent.SimpleRoutingParentViewImpl

object AppCustomisations : RibCustomisationDirectoryImpl() {

    init {
        +SimpleRoutingParent.Customisation(
            viewFactory = SimpleRoutingParentViewImpl.Factory(R.layout.rib_customisation_parent)
        )
        SimpleRoutingParent::class {
            +SimpleRoutingChild1.Customisation(
                viewFactory = SimpleRoutingChild1ViewImpl.Factory(R.layout.rib_customisation_child1)
            )
            +SimpleRoutingChild2.Customisation(
                viewFactory = SimpleRoutingChild2ViewImpl.Factory(R.layout.rib_customisation_child2)
            )
            +SimpleRoutingChild1Child1.Customisation(
                viewFactory = SimpleRoutingChild1Child1ViewImpl.Factory(R.layout.rib_customisation_child1_child1)
            )
            +SimpleRoutingChild1Child2.Customisation(
                viewFactory = SimpleRoutingChild1Child2ViewImpl.Factory(R.layout.rib_customisation_child1_child2)
            )
        }
    }
}
