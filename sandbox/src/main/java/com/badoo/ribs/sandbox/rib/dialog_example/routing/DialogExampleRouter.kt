package com.badoo.ribs.sandbox.rib.dialog_example.routing

import android.os.Parcelable
import com.badoo.ribs.android.dialog.DialogLauncher
import com.badoo.ribs.android.dialog.routing.resolution.DialogResolution.Companion.showDialog
import com.badoo.ribs.core.modality.BuildParams
import com.badoo.ribs.routing.Routing
import com.badoo.ribs.routing.resolution.Resolution
import com.badoo.ribs.routing.resolution.Resolution.Companion.noop
import com.badoo.ribs.routing.router.Router
import com.badoo.ribs.routing.source.RoutingSource
import com.badoo.ribs.sandbox.rib.dialog_example.dialog.Dialogs
import com.badoo.ribs.sandbox.rib.dialog_example.routing.DialogExampleRouter.Configuration
import com.badoo.ribs.sandbox.rib.dialog_example.routing.DialogExampleRouter.Configuration.Content
import com.badoo.ribs.sandbox.rib.dialog_example.routing.DialogExampleRouter.Configuration.Overlay
import kotlinx.android.parcel.Parcelize

class DialogExampleRouter(
    buildParams: BuildParams<Nothing?>,
    routingSource: RoutingSource<Configuration>,
    private val dialogLauncher: DialogLauncher,
    private val dialogs: Dialogs
): Router<Configuration>(
    buildParams = buildParams,
    routingSource = routingSource
) {

    sealed class Configuration : Parcelable {
        sealed class Content : Configuration() {
            @Parcelize object Default : Content()
        }
        sealed class Overlay : Configuration() {
            @Parcelize object ThemeDialog : Overlay()
            @Parcelize object SimpleDialog : Overlay()
            @Parcelize object LazyDialog : Overlay()
            @Parcelize object RibDialog : Overlay()
        }
    }

    // TODO consider configuration id as second parameter
    override fun resolve(routing: Routing<Configuration>): Resolution =
        when (routing.configuration) {
            is Content.Default -> noop()
            // TODO can be done with factory to remove first 3 params and make it simpler
            is Overlay.ThemeDialog -> showDialog(routingSource, routing.identifier, dialogLauncher, dialogs.themeDialog)
            is Overlay.SimpleDialog -> showDialog(routingSource, routing.identifier, dialogLauncher, dialogs.simpleDialog)
            is Overlay.LazyDialog -> showDialog(routingSource, routing.identifier, dialogLauncher, dialogs.lazyDialog)
            is Overlay.RibDialog -> showDialog(routingSource, routing.identifier, dialogLauncher, dialogs.ribDialog)
        }
}
