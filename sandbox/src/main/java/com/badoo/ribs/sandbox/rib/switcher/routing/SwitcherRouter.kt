package com.badoo.ribs.sandbox.rib.switcher.routing

import android.os.Parcelable
import com.badoo.ribs.android.dialog.DialogLauncher
import com.badoo.ribs.android.dialog.routing.resolution.DialogResolution.Companion.showDialog
import com.badoo.ribs.core.modality.BuildParams
import com.badoo.ribs.routing.Routing
import com.badoo.ribs.routing.resolution.ChildResolution.Companion.child
import com.badoo.ribs.routing.resolution.Resolution
import com.badoo.ribs.routing.router.Router
import com.badoo.ribs.routing.source.RoutingSource
import com.badoo.ribs.routing.source.RoutingSource.Companion.permanent
import com.badoo.ribs.routing.transition.handler.TransitionHandler
import com.badoo.ribs.sandbox.rib.switcher.dialog.DialogToTestOverlay
import com.badoo.ribs.sandbox.rib.switcher.routing.SwitcherRouter.Configuration
import com.badoo.ribs.sandbox.rib.switcher.routing.SwitcherRouter.Configuration.Content
import com.badoo.ribs.sandbox.rib.switcher.routing.SwitcherRouter.Configuration.Overlay
import com.badoo.ribs.sandbox.rib.switcher.routing.SwitcherRouter.Configuration.Permanent
import kotlinx.android.parcel.Parcelize

class SwitcherRouter internal constructor(
    buildParams: BuildParams<Nothing?>,
    routingSource: RoutingSource<Configuration>,
    transitionHandler: TransitionHandler<Configuration>? = null,
    private val builders: SwitcherChildBuilders,
    private val dialogLauncher: DialogLauncher,
    private val dialogToTestOverlay: DialogToTestOverlay
): Router<Configuration>(
    buildParams = buildParams,
    routingSource = routingSource + permanent(Permanent.Menu),
    transitionHandler = transitionHandler
) {
    sealed class Configuration : Parcelable {
        sealed class Permanent : Configuration() {
            @Parcelize object Menu : Permanent()
        }
        sealed class Content : Configuration() {
            @Parcelize object Hello : Content()
            @Parcelize object Foo : Content()
            @Parcelize object DialogsExample : Content()
            @Parcelize object Compose : Content()
            @Parcelize object Blocker : Content()
        }
        sealed class Overlay : Configuration() {
            @Parcelize object Dialog : Overlay()
        }
    }

    override fun resolve(routing: Routing<Configuration>): Resolution =
        with(builders) {
            when (routing.configuration) {
                is Permanent.Menu -> child { menu.build(it) }
                is Content.Hello -> child { helloWorld.build(it) }
                is Content.Foo -> child { fooBar.build(it) }
                is Content.DialogsExample -> child {  dialogExample.build(it) }
                is Content.Compose -> child { composeParent.build(it) }
                is Content.Blocker -> child { blocker.build(it) }
                is Overlay.Dialog -> showDialog(routingSource, routing.identifier, dialogLauncher, dialogToTestOverlay)
            }
        }
}

