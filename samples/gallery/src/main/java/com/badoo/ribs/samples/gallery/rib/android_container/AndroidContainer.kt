package com.badoo.ribs.samples.gallery.rib.android_container

import com.badoo.ribs.core.Rib
import com.badoo.ribs.core.customisation.RibCustomisation
import com.badoo.ribs.routing.transition.handler.TransitionHandler
import com.badoo.ribs.rx2.clienthelper.connector.Connectable
import com.badoo.ribs.samples.gallery.rib.android_container.AndroidContainer.Input
import com.badoo.ribs.samples.gallery.rib.android_container.AndroidContainer.Output
import com.badoo.ribs.samples.gallery.rib.android_container.routing.AndroidContainerRouter

interface AndroidContainer : Rib, Connectable<Input, Output> {

    interface Dependency

    sealed class Input

    sealed class Output

    class Customisation(
        val viewFactory: AndroidContainerView.Factory = AndroidContainerViewImpl.Factory(),
        val transitionHandler: TransitionHandler<AndroidContainerRouter.Configuration>? = null
    ) : RibCustomisation
}
