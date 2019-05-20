package com.badoo.ribs.test.util.ribs.root

import android.os.Parcelable
import com.badoo.ribs.core.Node
import com.badoo.ribs.core.Router
import com.badoo.ribs.core.routing.action.AttachRibRoutingAction.Companion.attach
import com.badoo.ribs.core.routing.action.CompositeRoutingAction
import com.badoo.ribs.core.routing.action.DialogRoutingAction.Companion.showDialog
import com.badoo.ribs.core.routing.action.RoutingAction
import com.badoo.ribs.core.routing.action.RoutingAction.Companion.noop
import com.badoo.ribs.dialog.DialogLauncher
import com.badoo.ribs.test.util.ribs.TestRibDialog
import com.badoo.ribs.test.util.ribs.root.TestRootRouter.Configuration
import com.badoo.ribs.test.util.ribs.root.TestRootRouter.Configuration.Content
import com.badoo.ribs.test.util.ribs.root.TestRootRouter.Configuration.Overlay
import com.badoo.ribs.test.util.ribs.root.TestRootRouter.Configuration.Permanent
import kotlinx.android.parcel.Parcelize

class TestRootRouter(
    private val builder1: () -> Node<*>,
    private val builder2: () -> Node<*>,
    private val dialogLauncher: DialogLauncher,
    permanentParts: List<Permanent>,
    initialConfiguration: Content
) : Router<Configuration, Permanent, Content, Overlay, TestRootView>(
    initialConfiguration = initialConfiguration,
    permanentParts = permanentParts
) {

    sealed class Configuration : Parcelable {
        sealed class Permanent : Configuration() {

        }

        sealed class Content : Configuration() {
            @Parcelize
            object NoOp : Content()

            @Parcelize
            object AttachNode1 : Content()

            @Parcelize
            object AttachNode2 : Content()

            @Parcelize
            object AttachNode1And2 : Content()

            @Parcelize
            object AttachNode1AsDialog : Content()

            @Parcelize
            object AttachNode2AsDialog : Content()
        }

        sealed class Overlay : Configuration() {
            @Parcelize
            object AttachNode1AsOverlay : Overlay()

            @Parcelize
            object AttachNode2AsOverlay : Overlay()

            @Parcelize
            object AttachNode1AsDialogAndOverlay : Overlay()

            @Parcelize
            object AttachNode2AsDialogAndOverlay : Overlay()
        }
    }

    override fun resolveConfiguration(configuration: Configuration): RoutingAction<TestRootView> =
        when (configuration) {
            Content.NoOp -> noop()
            Content.AttachNode1 -> attach(builder1)
            Content.AttachNode2 -> attach(builder2)
            Content.AttachNode1AsDialog -> showDialog(this, dialogLauncher, TestRibDialog(builder1))
            Content.AttachNode2AsDialog -> showDialog(this, dialogLauncher, TestRibDialog(builder2))
            Content.AttachNode1And2 -> CompositeRoutingAction.composite(
                attach(builder1),
                attach(builder2)
            )
            Overlay.AttachNode1AsOverlay -> attach(builder1)
            Overlay.AttachNode2AsOverlay -> attach(builder2)
            Overlay.AttachNode1AsDialogAndOverlay -> showDialog(this, dialogLauncher, TestRibDialog(builder1))
            Overlay.AttachNode2AsDialogAndOverlay -> showDialog(this, dialogLauncher, TestRibDialog(builder2))
        }
}
