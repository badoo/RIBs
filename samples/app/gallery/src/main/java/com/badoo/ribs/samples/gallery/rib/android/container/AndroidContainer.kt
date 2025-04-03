package com.badoo.ribs.samples.gallery.rib.android.container

import com.badoo.ribs.android.activitystarter.CanProvideActivityStarter
import com.badoo.ribs.android.dialog.CanProvideDialogLauncher
import com.badoo.ribs.android.permissionrequester.CanProvidePermissionRequester
import com.badoo.ribs.core.Rib
import com.badoo.ribs.core.customisation.RibCustomisation
import com.badoo.ribs.routing.transition.handler.TransitionHandler
import com.badoo.ribs.rx3.clienthelper.connector.Connectable
import com.badoo.ribs.samples.gallery.rib.android.container.AndroidContainer.Input
import com.badoo.ribs.samples.gallery.rib.android.container.AndroidContainer.Output
import com.badoo.ribs.samples.gallery.rib.android.container.routing.AndroidContainerRouter

interface AndroidContainer : Rib, Connectable<Input, Output> {

    interface Dependency :
        CanProvideActivityStarter,
        CanProvidePermissionRequester,
        CanProvideDialogLauncher

    sealed class Input

    sealed class Output

    class Customisation(
        val viewFactory: AndroidContainerView.Factory = AndroidContainerViewImpl.Factory(),
        val transitionHandler: TransitionHandler<AndroidContainerRouter.Configuration>? = null
    ) : RibCustomisation
}
