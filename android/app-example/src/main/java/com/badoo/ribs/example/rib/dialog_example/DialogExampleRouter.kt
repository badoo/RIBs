package com.badoo.ribs.example.rib.dialog_example

import android.os.Parcelable
import com.badoo.ribs.core.Router
import com.badoo.ribs.core.routing.action.DialogRoutingAction.Companion.dialog
import com.badoo.ribs.core.routing.action.RoutingAction
import com.badoo.ribs.dialog.DialogLauncher
import com.badoo.ribs.example.rib.dialog_example.DialogExampleRouter.Configuration
import com.badoo.ribs.example.rib.dialog_example.dialog.SimpleDialog
import com.badoo.ribs.example.rib.dialog_example.dialog.RibDialog
import kotlinx.android.parcel.Parcelize

class DialogExampleRouter(
    private val dialogLauncher: DialogLauncher,
    private val simpleDialog: SimpleDialog,
    private val ribDialog: RibDialog
): Router<Configuration, DialogExampleView>(
    initialConfiguration = Configuration.Default
) {
    sealed class Configuration : Parcelable {
        @Parcelize object Default : Configuration()
        @Parcelize object SimpleDialog : Configuration()
        @Parcelize object RibDialog : Configuration()
    }

    override fun resolveConfiguration(configuration: Configuration): RoutingAction<DialogExampleView> =
        when (configuration) {
            Configuration.Default -> RoutingAction.noop()
            Configuration.SimpleDialog -> dialog(dialogLauncher, simpleDialog)
            Configuration.RibDialog -> dialog(dialogLauncher, ribDialog)
        }
}
