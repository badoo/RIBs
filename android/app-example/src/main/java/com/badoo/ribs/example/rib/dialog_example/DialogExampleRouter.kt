package com.badoo.ribs.example.rib.dialog_example

import android.os.Parcelable
import com.badoo.ribs.core.Router
import com.badoo.ribs.core.routing.action.DialogRoutingAction.Companion.showDialog
import com.badoo.ribs.core.routing.action.RoutingAction
import com.badoo.ribs.core.routing.action.RoutingAction.Companion.noop
import com.badoo.ribs.dialog.DialogLauncher
import com.badoo.ribs.example.rib.dialog_example.DialogExampleRouter.Configuration
import com.badoo.ribs.example.rib.dialog_example.dialog.LazyDialog
import com.badoo.ribs.example.rib.dialog_example.dialog.SimpleDialog
import com.badoo.ribs.example.rib.dialog_example.dialog.RibDialog
import kotlinx.android.parcel.Parcelize

class DialogExampleRouter(
    private val dialogLauncher: DialogLauncher,
    private val simpleDialog: SimpleDialog,
    private val lazyDialog: LazyDialog,
    private val ribDialog: RibDialog
): Router<Configuration, DialogExampleView>(
    initialConfiguration = Configuration.Default
) {
    sealed class Configuration : Parcelable {
        @Parcelize object Default : Configuration()
        @Parcelize object SimpleDialog : Configuration()
        @Parcelize object LazyDialog : Configuration()
        @Parcelize object RibDialog : Configuration()
    }

    override fun resolveConfiguration(configuration: Configuration): RoutingAction<DialogExampleView> =
        when (configuration) {
            is Configuration.Default -> noop()
            is Configuration.SimpleDialog -> showDialog(dialogLauncher, simpleDialog)
            is Configuration.LazyDialog -> showDialog(dialogLauncher, lazyDialog)
            is Configuration.RibDialog -> showDialog(dialogLauncher, ribDialog)
        }
}
