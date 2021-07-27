package com.badoo.ribs.samples.gallery.rib.root_container

import com.badoo.ribs.rx2.clienthelper.connector.Connectable
import com.badoo.ribs.core.Rib
import com.badoo.ribs.routing.transition.handler.TransitionHandler
import com.badoo.ribs.core.customisation.RibCustomisation
import com.badoo.ribs.samples.gallery.rib.root_picker.RootPicker
import com.badoo.ribs.samples.gallery.rib.root_container.RootContainer.Input
import com.badoo.ribs.samples.gallery.rib.root_container.RootContainer.Output
import com.badoo.ribs.samples.gallery.rib.root_container.routing.RootContainerRouter
import io.reactivex.Single

interface RootContainer : Rib, Connectable<Input, Output> {

    interface Dependency

    sealed class Input

    sealed class Output

    class Customisation(
        val viewFactory: RootContainerView.Factory = RootContainerViewImpl.Factory(),
        val transitionHandler: TransitionHandler<RootContainerRouter.Configuration>? = null
    ) : RibCustomisation

    fun attachRootPicker(): Single<RootPicker>
}
