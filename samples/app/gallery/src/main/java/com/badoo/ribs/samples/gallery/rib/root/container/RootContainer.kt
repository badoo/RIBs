package com.badoo.ribs.samples.gallery.rib.root.container

import com.badoo.ribs.android.activitystarter.CanProvideActivityStarter
import com.badoo.ribs.android.dialog.CanProvideDialogLauncher
import com.badoo.ribs.android.permissionrequester.CanProvidePermissionRequester
import com.badoo.ribs.rx2.clienthelper.connector.Connectable
import com.badoo.ribs.core.Rib
import com.badoo.ribs.routing.transition.handler.TransitionHandler
import com.badoo.ribs.samples.gallery.rib.root.picker.RootPicker
import com.badoo.ribs.samples.gallery.rib.root.container.RootContainer.Input
import com.badoo.ribs.samples.gallery.rib.root.container.RootContainer.Output
import com.badoo.ribs.samples.gallery.rib.root.container.routing.RootContainerRouter
import com.bumble.appyx.utils.customisations.NodeCustomisation
import io.reactivex.Single

interface RootContainer : Rib, Connectable<Input, Output> {

    interface Dependency :
        CanProvideActivityStarter,
        CanProvidePermissionRequester,
        CanProvideDialogLauncher

    sealed class Input

    sealed class Output

    class Customisation(
        val viewFactory: RootContainerView.Factory = RootContainerViewImpl.Factory(),
        val transitionHandler: TransitionHandler<RootContainerRouter.Configuration>? = null
    ) : NodeCustomisation

    fun attachRootPicker(): Single<RootPicker>
}
