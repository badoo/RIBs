package com.badoo.ribs.test.util.ribs.root

import android.os.Parcelable
import com.badoo.ribs.core.Node
import com.badoo.ribs.core.Router
import com.badoo.ribs.core.routing.action.AttachRibRoutingAction.Companion.attach
import com.badoo.ribs.core.routing.action.CompositeRoutingAction
import com.badoo.ribs.core.routing.action.DialogRoutingAction.Companion.showDialog
import com.badoo.ribs.core.routing.action.InvokeOnExecute
import com.badoo.ribs.core.routing.action.RoutingAction
import com.badoo.ribs.core.routing.action.RoutingAction.Companion.noop
import com.badoo.ribs.dialog.DialogLauncher
import com.badoo.ribs.test.util.ribs.TestRibDialog
import com.badoo.ribs.test.util.ribs.root.TestRootRouter.Configuration
import kotlinx.android.parcel.Parcelize

class TestRootRouter(
    private val builder1: () -> Node<*>,
    private val builder2: () -> Node<*>,
    private val dialogLauncher: DialogLauncher,
    override val permanentParts: List<() -> Node<*>>,
    initialConfiguration: Configuration
) : Router<Configuration, TestRootView>(
    initialConfiguration = initialConfiguration
) {

    sealed class Configuration : Parcelable {
        @Parcelize
        object NoOp : Configuration()

        @Parcelize
        object AttachNode1 : Configuration()

        @Parcelize
        object AttachNode2 : Configuration()

        @Parcelize
        object AttachNode1And2 : Configuration()

        @Parcelize
        object AttachNode1AsDialog : Configuration()

        @Parcelize
        object AttachNode2AsDialog : Configuration()

        @Parcelize
        object AttachNode1AsOverlay : Configuration(), Overlay

        @Parcelize
        object AttachNode2AsOverlay : Configuration(), Overlay

        @Parcelize
        object AttachNode1AsDialogAndOverlay : Configuration(), Overlay

        @Parcelize
        object AttachNode2AsDialogAndOverlay : Configuration(), Overlay
    }

    override fun resolveConfiguration(configuration: Configuration): RoutingAction<TestRootView> =
        when (configuration) {
            Configuration.NoOp -> noop()
            Configuration.AttachNode1 -> attach(builder1)
            Configuration.AttachNode2 -> attach(builder2)
            Configuration.AttachNode1AsDialog -> showDialog(this, dialogLauncher, TestRibDialog(builder1))
            Configuration.AttachNode2AsDialog -> showDialog(this, dialogLauncher, TestRibDialog(builder2))
            Configuration.AttachNode1AsOverlay -> attach(builder1)
            Configuration.AttachNode2AsOverlay -> attach(builder2)
            Configuration.AttachNode1AsDialogAndOverlay -> showDialog(this, dialogLauncher, TestRibDialog(builder1))
            Configuration.AttachNode2AsDialogAndOverlay -> showDialog(this, dialogLauncher, TestRibDialog(builder2))
            Configuration.AttachNode1And2 -> CompositeRoutingAction.composite(
                attach(builder1),
                attach(builder2)
            )
        }
}
