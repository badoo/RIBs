package com.badoo.ribs.samples.gallery.rib.routing_container

import com.badoo.ribs.core.Rib
import com.badoo.ribs.core.customisation.RibCustomisation
import com.badoo.ribs.routing.transition.handler.TransitionHandler
import com.badoo.ribs.rx2.clienthelper.connector.Connectable
import com.badoo.ribs.samples.gallery.rib.routing_container.RoutingContainer.Input
import com.badoo.ribs.samples.gallery.rib.routing_container.RoutingContainer.Output
import com.badoo.ribs.samples.gallery.rib.routing_container.routing.RoutingContainerRouter

interface RoutingContainer : Rib, Connectable<Input, Output> {

    interface Dependency

    sealed class Input

    sealed class Output

    class Customisation(
        val viewFactory: RoutingContainerView.Factory = RoutingContainerViewImpl.Factory(),
        val transitionHandler: TransitionHandler<RoutingContainerRouter.Configuration>? = null
    ) : RibCustomisation
}
