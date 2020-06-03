package com.badoo.ribs.portal

import android.os.Bundle
import android.os.Parcelable
import com.badoo.ribs.core.AncestryInfo
import com.badoo.ribs.core.Rib
import com.badoo.ribs.core.routing.router.Router
import com.badoo.ribs.core.builder.BuildContext
import com.badoo.ribs.core.builder.BuildParams
import com.badoo.ribs.core.routing.source.RoutingSource
import com.badoo.ribs.core.routing.action.RoutingAction
import com.badoo.ribs.core.routing.resolver.RoutingResolver
import com.badoo.ribs.core.routing.Routing
import com.badoo.ribs.portal.PortalRouter.Configuration
import com.badoo.ribs.portal.PortalRouter.Configuration.Content
import com.badoo.ribs.portal.PortalRouter.Configuration.Overlay
import com.badoo.ribs.core.routing.transition.handler.TransitionHandler
import com.badoo.ribs.customisation.RibCustomisationDirectoryImpl
import kotlinx.android.parcel.Parcelize

class PortalRouter(
    buildParams: BuildParams<Nothing?>,
    routingSource: RoutingSource<Configuration>,
    private val defaultRoutingAction: RoutingAction,
    transitionHandler: TransitionHandler<Configuration>? = null
): Router<Configuration>(
    buildParams = buildParams,
    routingSource = routingSource,
    transitionHandler = transitionHandler
) {
    sealed class Configuration : Parcelable {
        sealed class Content : Configuration() {
            @Parcelize object Default : Content()
            // TODO List<RoutingElement>
            @Parcelize data class Portal(val configurationChain: List<Parcelable>) : Content()
        }
        sealed class Overlay : Configuration() {
            @Parcelize data class Portal(val configurationChain: List<Parcelable>) : Overlay()
        }
    }

    override fun resolve(routing: Routing<Configuration>): RoutingAction =
        when (val configuration = routing.configuration) {
            is Content.Default -> defaultRoutingAction
            is Content.Portal -> configuration.configurationChain.resolve()
            is Overlay.Portal -> configuration.configurationChain.resolve()
        }

    // TODO probably needs to change from List<Parcelable> to List<AncestryInfo>,
    //  so that extra info can be added too. See below for details.
    private fun List<Parcelable>.resolve(): RoutingAction {
        // TODO grab first from real root (now should be possible) -- currently works only if PortalRouter is in the root rib
        var targetRouter: RoutingResolver<Parcelable> =
            this@PortalRouter as RoutingResolver<Parcelable>
        var routingAction: RoutingAction = targetRouter.resolve(Routing(first()))

        drop(1).forEach { element ->
            val bundles = emptyList<Bundle?>()

            // TODO don't build it again if already available as child.
            //  This probably means storing Node identifier in addition to (Parcelable) configuration.
            val ribs = buildStep(routingAction)

            // TODO having 0 nodes is an impossible scenario, but having more than 1 can be valid.
            //  Solution is again to store Node identifiers & Bundles that help picking the correct one.
            val rib = ribs.first()

            rib.node.plugin<RoutingResolver<Parcelable>>()?.let {
                targetRouter = it
            } ?: throw IllegalStateException("Invalid chain of parents. This should never happen. Chain: $this")

            routingAction = targetRouter.resolve(
                Routing(element)
            )
        }

        return routingAction
    }

    private fun buildStep(routingAction: RoutingAction): List<Rib> {
        return routingAction.buildNodes(
            listOf(
                BuildContext(
                    ancestryInfo = AncestryInfo.Root, // we'll be discarding these Nodes, it doesn't matter
                    // TODO for maximum correctness, original List<> should also contain Bundles,
                    //  as that might change how dependencies are built (right now there's no case for this,
                    //  but can be in the future).
                    savedInstanceState = null,
                    customisations = RibCustomisationDirectoryImpl()
                )
            )
        )
    }
}
