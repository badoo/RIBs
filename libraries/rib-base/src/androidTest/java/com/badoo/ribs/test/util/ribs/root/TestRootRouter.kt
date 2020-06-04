package com.badoo.ribs.test.util.ribs.root

import android.os.Parcelable
import com.badoo.ribs.core.builder.BuildParams
import com.badoo.ribs.core.builder.NodeFactory
import com.badoo.ribs.core.routing.action.AttachRibRoutingAction.Companion.attach
import com.badoo.ribs.core.routing.action.CompositeRoutingAction.Companion.composite
import com.badoo.ribs.android.dialog.routing.action.DialogRoutingAction.Companion.showDialog
import com.badoo.ribs.core.routing.action.RoutingAction
import com.badoo.ribs.core.routing.action.RoutingAction.Companion.noop
import com.badoo.ribs.core.routing.Routing
import com.badoo.ribs.core.routing.router.Router
import com.badoo.ribs.core.routing.source.RoutingSource
import com.badoo.ribs.core.routing.source.RoutingSource.Companion.permanent
import com.badoo.ribs.android.dialog.DialogLauncher
import com.badoo.ribs.test.util.ribs.TestRibDialog
import com.badoo.ribs.test.util.ribs.root.TestRootRouter.Configuration
import com.badoo.ribs.test.util.ribs.root.TestRootRouter.Configuration.Content
import com.badoo.ribs.test.util.ribs.root.TestRootRouter.Configuration.Overlay
import com.badoo.ribs.test.util.ribs.root.TestRootRouter.Configuration.Permanent
import kotlinx.android.parcel.Parcelize

class TestRootRouter(
    buildParams: BuildParams<*>,
    routingSource: RoutingSource<Configuration>,
    private val builderPermanent1: NodeFactory,
    private val builderPermanent2: NodeFactory,
    private val builder3: NodeFactory,
    private val builder1: NodeFactory,
    private val builder2: NodeFactory,
    private val dialogLauncher: DialogLauncher,
    permanentParts: List<Permanent>
) : Router<Configuration>(
    buildParams = buildParams,
    routingSource = routingSource + permanent(permanentParts)
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

    override fun resolve(routing: Routing<Configuration>): RoutingAction =
        when (routing.configuration) {
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
            Overlay.AttachNode1AsOverlay -> showDialog(routingSource, routing.identifier, dialogLauncher, TestRibDialog(builder1))
            Overlay.AttachNode2AsOverlay -> showDialog(routingSource, routing.identifier, dialogLauncher, TestRibDialog(builder2))
            Overlay.AttachNode3AsOverlay -> showDialog(routingSource, routing.identifier, dialogLauncher, TestRibDialog(builder3))
        }
}
