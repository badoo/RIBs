package com.badoo.ribs.samples.dialogs.rib.dialogs_example

import android.os.Parcelable
import com.badoo.ribs.android.dialog.DialogLauncher
import com.badoo.ribs.android.dialog.routing.resolution.DialogResolution.Companion.showDialog
import com.badoo.ribs.core.modality.BuildParams
import com.badoo.ribs.routing.Routing
import com.badoo.ribs.routing.resolution.Resolution
import com.badoo.ribs.routing.router.Router
import com.badoo.ribs.routing.source.RoutingSource
import com.badoo.ribs.samples.dialogs.dialogs.Dialogs
import com.badoo.ribs.samples.dialogs.rib.dialogs_example.DialogsRouter.Configuration
import com.badoo.ribs.samples.dialogs.rib.dialogs_example.DialogsRouter.Configuration.Content
import com.badoo.ribs.samples.dialogs.rib.dialogs_example.DialogsRouter.Configuration.Overlay
import kotlinx.parcelize.Parcelize

class DialogsRouter(
    buildParams: BuildParams<Nothing?>,
    routingSource: RoutingSource<Configuration>,
    private val dialogLauncher: DialogLauncher,
    private val dialogs: Dialogs
) : Router<Configuration>(
    buildParams = buildParams,
    routingSource = routingSource
) {

    sealed class Configuration : Parcelable {
        sealed class Content : Configuration() {
            @Parcelize
            object Default : Content()
        }

        sealed class Overlay : Configuration() {
            @Parcelize
            object ThemedDialog : Overlay()

            @Parcelize
            object SimpleDialog : Overlay()

            @Parcelize
            object LazyDialog : Overlay()

            @Parcelize
            object RibDialog : Overlay()
        }
    }

    override fun resolve(routing: Routing<Configuration>): Resolution =
        when (routing.configuration) {
            is Content.Default -> Resolution.noop()
            is Overlay.ThemedDialog -> showDialog(routingSource, routing.identifier, dialogLauncher, dialogs.themedDialog)
            is Overlay.SimpleDialog -> showDialog(routingSource, routing.identifier, dialogLauncher, dialogs.simpleDialog)
            is Overlay.LazyDialog -> showDialog(routingSource, routing.identifier, dialogLauncher, dialogs.lazyDialog)
            is Overlay.RibDialog -> showDialog(routingSource, routing.identifier, dialogLauncher, dialogs.ribDialog)
        }
}
