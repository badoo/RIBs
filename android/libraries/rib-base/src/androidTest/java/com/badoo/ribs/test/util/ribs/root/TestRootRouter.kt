package com.badoo.ribs.test.util.ribs.root

import com.badoo.ribs.core.BuildContext
import android.os.Parcelable
import com.badoo.ribs.core.Node
import com.badoo.ribs.core.Router
import com.badoo.ribs.core.routing.action.AttachRibRoutingAction.Companion.attach
import com.badoo.ribs.core.routing.action.CompositeRoutingAction.Companion.composite
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
    buildContext: BuildContext.Resolved<Nothing?>,
    private val builderPermanent1: (BuildContext.Params) -> Node<*>,
    private val builderPermanent2: (BuildContext.Params) -> Node<*>,
    private val builder3: (BuildContext.Params) -> Node<*>,
    private val builder1: (BuildContext.Params) -> Node<*>,
    private val builder2: (BuildContext.Params) -> Node<*>,
    private val dialogLauncher: DialogLauncher,
    permanentParts: List<Permanent>,
    initialConfiguration: Content
) : Router<Configuration, Permanent, Content, Overlay, TestRootView>(
    buildContext = buildContext,
    initialConfiguration = initialConfiguration,
    permanentParts = permanentParts
) {

    sealed class Configuration : Parcelable {
        sealed class Permanent : Configuration() {
            @Parcelize object Permanent1 : Permanent()
            @Parcelize object Permanent2 : Permanent()
        }

        sealed class Content : Configuration() {
            @Parcelize object NoOp : Content()
            @Parcelize object AttachNode1 : Content()
            @Parcelize object AttachNode2 : Content()
            @Parcelize object AttachNode3 : Content()
            @Parcelize object AttachNode1And2 : Content()
        }

        sealed class Overlay : Configuration() {
            @Parcelize object AttachNode1AsOverlay : Overlay()
            @Parcelize object AttachNode2AsOverlay : Overlay()
            @Parcelize object AttachNode3AsOverlay : Overlay()
        }
    }

    override fun resolveConfiguration(configuration: Configuration): RoutingAction<TestRootView> =
        when (configuration) {
            Permanent.Permanent1 -> attach(builderPermanent1)
            Permanent.Permanent2 -> attach(builderPermanent2)
            Content.NoOp -> noop()
            Content.AttachNode1 -> attach(builder1)
            Content.AttachNode2 -> attach(builder2)
            Content.AttachNode3 -> attach(builder3)
            Content.AttachNode1And2 -> composite(
                attach(builder1),
                attach(builder2)
            )
            Overlay.AttachNode1AsOverlay -> showDialog(this, dialogLauncher, TestRibDialog(builder1))
            Overlay.AttachNode2AsOverlay -> showDialog(this, dialogLauncher, TestRibDialog(builder2))
            Overlay.AttachNode3AsOverlay -> showDialog(this, dialogLauncher, TestRibDialog(builder3))
        }
}
