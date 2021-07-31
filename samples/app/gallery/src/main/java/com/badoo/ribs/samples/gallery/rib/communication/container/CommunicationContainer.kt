package com.badoo.ribs.samples.gallery.rib.communication.container

import com.badoo.ribs.core.Rib
import com.badoo.ribs.core.customisation.RibCustomisation
import com.badoo.ribs.routing.transition.handler.TransitionHandler
import com.badoo.ribs.rx2.clienthelper.connector.Connectable
import com.badoo.ribs.samples.gallery.rib.communication.container.CommunicationContainer.Input
import com.badoo.ribs.samples.gallery.rib.communication.container.CommunicationContainer.Output
import com.badoo.ribs.samples.gallery.rib.communication.container.routing.CommunicationContainerRouter

interface CommunicationContainer : Rib, Connectable<Input, Output> {

    interface Dependency

    sealed class Input

    sealed class Output

    class Customisation(
        val viewFactory: CommunicationContainerView.Factory = CommunicationContainerViewImpl.Factory(),
        val transitionHandler: TransitionHandler<CommunicationContainerRouter.Configuration>? = null
    ) : RibCustomisation
}
