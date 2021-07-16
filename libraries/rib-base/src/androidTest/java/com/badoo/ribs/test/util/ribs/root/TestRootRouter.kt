package com.badoo.ribs.test.util.ribs.root

import android.os.Parcelable
import com.badoo.ribs.android.dialog.DialogLauncher
import com.badoo.ribs.android.dialog.routing.resolution.DialogResolution.Companion.showDialog
import com.badoo.ribs.core.modality.BuildParams
import com.badoo.ribs.routing.Routing
import com.badoo.ribs.routing.resolution.ChildResolution.Companion.child
import com.badoo.ribs.routing.resolution.CompositeResolution.Companion.composite
import com.badoo.ribs.routing.resolution.Resolution
import com.badoo.ribs.routing.resolution.Resolution.Companion.noop
import com.badoo.ribs.routing.resolution.RibFactory
import com.badoo.ribs.routing.router.Router
import com.badoo.ribs.routing.source.RoutingSource
import com.badoo.ribs.routing.source.RoutingSource.Companion.permanent
import com.badoo.ribs.routing.transition.handler.TransitionHandler
import com.badoo.ribs.test.util.ribs.TestRibDialog
import com.badoo.ribs.test.util.ribs.root.TestRootRouter.Configuration
import com.badoo.ribs.test.util.ribs.root.TestRootRouter.Configuration.*
import kotlinx.parcelize.Parcelize

class TestRootRouter(
    buildParams: BuildParams<*>,
    routingSource: RoutingSource<Configuration>,
    private val builderPermanent1: RibFactory,
    private val builderPermanent2: RibFactory,
    private val builder3: RibFactory,
    private val builder1: RibFactory,
    private val builder2: RibFactory,
    private val dialogLauncher: DialogLauncher,
    transitionHandler: TransitionHandler<Configuration>? = null,
    permanentParts: List<Permanent>
) : Router<Configuration>(
    buildParams = buildParams,
    routingSource = routingSource + permanent(permanentParts),
    transitionHandler = transitionHandler
) {

    sealed class Configuration : Parcelable {
        sealed class Permanent : Configuration() {
            @Parcelize
            object Permanent1 : Permanent()

            @Parcelize
            object Permanent2 : Permanent()
        }

        sealed class Content : Configuration() {
            @Parcelize
            object NoOp : Content()

            @Parcelize
            object AttachNode1 : Content()

            @Parcelize
            object AttachNode2 : Content()

            @Parcelize
            object AttachNode3 : Content()

            @Parcelize
            object AttachNode1And2 : Content()
        }

        sealed class Overlay : Configuration() {
            @Parcelize
            object AttachNode1AsOverlay : Overlay()

            @Parcelize
            object AttachNode2AsOverlay : Overlay()

            @Parcelize
            object AttachNode3AsOverlay : Overlay()
        }
    }

    override fun resolve(routing: Routing<Configuration>): Resolution =
        when (routing.configuration) {
            Permanent.Permanent1 -> child(builderPermanent1)
            Permanent.Permanent2 -> child(builderPermanent2)
            Content.NoOp -> noop()
            Content.AttachNode1 -> child(builder1)
            Content.AttachNode2 -> child(builder2)
            Content.AttachNode3 -> child(builder3)
            Content.AttachNode1And2 -> composite(
                child(builder1),
                child(builder2)
            )
            Overlay.AttachNode1AsOverlay -> showDialog(routingSource, routing.identifier, dialogLauncher, TestRibDialog(builder1))
            Overlay.AttachNode2AsOverlay -> showDialog(routingSource, routing.identifier, dialogLauncher, TestRibDialog(builder2))
            Overlay.AttachNode3AsOverlay -> showDialog(routingSource, routing.identifier, dialogLauncher, TestRibDialog(builder3))
        }
}
