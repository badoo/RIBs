package com.badoo.ribs.example.rib.dialog_example

import android.os.Bundle
import android.os.Parcelable
import com.badoo.ribs.core.Router
import com.badoo.ribs.core.routing.action.DialogRoutingAction.Companion.showDialog
import com.badoo.ribs.core.routing.action.RoutingAction
import com.badoo.ribs.core.routing.action.RoutingAction.Companion.noop
import com.badoo.ribs.dialog.DialogLauncher
import com.badoo.ribs.example.rib.dialog_example.DialogExampleRouter.Configuration
import com.badoo.ribs.example.rib.dialog_example.DialogExampleRouter.Configuration.Content
import com.badoo.ribs.example.rib.dialog_example.DialogExampleRouter.Configuration.Overlay
import com.badoo.ribs.example.rib.dialog_example.dialog.LazyDialog
import com.badoo.ribs.example.rib.dialog_example.dialog.RibDialog
import com.badoo.ribs.example.rib.dialog_example.dialog.SimpleDialog
import kotlinx.android.parcel.Parcelize

class DialogExampleRouter(
    savedInstanceState: Bundle?,
    private val dialogLauncher: DialogLauncher,
    private val simpleDialog: SimpleDialog,
    private val lazyDialog: LazyDialog,
    private val ribDialog: RibDialog
): Router<Configuration, Nothing, Content, Overlay, DialogExampleView>(
    savedInstanceState = savedInstanceState,
    initialConfiguration = Content.Default
) {
    sealed class Configuration : Parcelable {
        sealed class Content : Configuration() {
            @Parcelize object Default : Content()
        }
        sealed class Overlay : Configuration() {
            @Parcelize object SimpleDialog : Overlay()
            @Parcelize object LazyDialog : Overlay()
            @Parcelize object RibDialog : Overlay()
        }
    }

    override fun resolveConfiguration(configuration: Configuration): RoutingAction<DialogExampleView> =
        when (configuration) {
            is Content.Default -> noop()
            is Overlay.SimpleDialog -> showDialog(this, dialogLauncher, simpleDialog)
            is Overlay.LazyDialog -> showDialog(this, dialogLauncher, lazyDialog)
            is Overlay.RibDialog -> showDialog(this, dialogLauncher, ribDialog)
        }
}
