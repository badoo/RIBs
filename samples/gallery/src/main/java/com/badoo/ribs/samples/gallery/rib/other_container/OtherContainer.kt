package com.badoo.ribs.samples.gallery.rib.other_container

import com.badoo.ribs.core.Rib
import com.badoo.ribs.core.customisation.RibCustomisation
import com.badoo.ribs.routing.transition.handler.TransitionHandler
import com.badoo.ribs.rx2.clienthelper.connector.Connectable
import com.badoo.ribs.samples.gallery.rib.other_container.OtherContainer.Input
import com.badoo.ribs.samples.gallery.rib.other_container.OtherContainer.Output
import com.badoo.ribs.samples.gallery.rib.other_container.routing.OtherContainerRouter

interface OtherContainer : Rib, Connectable<Input, Output> {

    interface Dependency

    sealed class Input

    sealed class Output

    class Customisation(
        val viewFactory: OtherContainerView.Factory = OtherContainerViewImpl.Factory(),
        val transitionHandler: TransitionHandler<OtherContainerRouter.Configuration>? = null
    ) : RibCustomisation
}
