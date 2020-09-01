package com.badoo.ribs.portal

import android.os.Bundle
import android.os.Parcelable
import com.badoo.ribs.annotation.ExperimentalApi
import com.badoo.ribs.core.Rib
import com.badoo.ribs.core.customisation.RibCustomisationDirectoryImpl
import com.badoo.ribs.core.modality.BuildContext
import com.badoo.ribs.core.modality.BuildParams
import com.badoo.ribs.portal.PortalRouter.Configuration
import com.badoo.ribs.portal.PortalRouter.Configuration.Content
import com.badoo.ribs.portal.PortalRouter.Configuration.Overlay
import com.badoo.ribs.routing.Routing
import com.badoo.ribs.routing.resolution.Resolution
import com.badoo.ribs.routing.resolver.RoutingResolver
import com.badoo.ribs.routing.router.Router
import com.badoo.ribs.routing.source.RoutingSource
import com.badoo.ribs.routing.transition.handler.TransitionHandler
import kotlinx.android.parcel.Parcelize

@ExperimentalApi
class PortalRouter(
    buildParams: BuildParams<Nothing?>,
    routingSource: RoutingSource<Configuration>,
    private val defaultResolution: Resolution,
    transitionHandler: TransitionHandler<Configuration>? = null
): Router<Configuration>(
    buildParams = buildParams,
    routingSource = routingSource,
    transitionHandler = transitionHandler
) {
    @ExperimentalApi
    sealed class Configuration : Parcelable {
        sealed class Content : Configuration() {
            @Parcelize object Default : Content()
            @Parcelize data class Portal(val routingChain: List<Routing<out Parcelable>>) : Content()
        }
        sealed class Overlay : Configuration() {
            @Parcelize data class Portal(val routingChain: List<Routing<out Parcelable>>) : Overlay()
        }
    }

    override fun resolve(routing: Routing<Configuration>): Resolution =
        when (val configuration = routing.configuration) {
            is Content.Default -> defaultResolution
            is Content.Portal -> configuration.routingChain.resolve()
            is Overlay.Portal -> configuration.routingChain.resolve()
        }

    // TODO probably needs to change from List<Parcelable> to List<AncestryInfo>,
    //  so that extra info can be added too. See below for details.
    private fun List<Routing<out Parcelable>>.resolve(): Resolution {
        // TODO grab first from real root (now should be possible) -- currently works only if PortalRouter is in the root rib
        var targetRouter: RoutingResolver<Parcelable> = this@PortalRouter as RoutingResolver<Parcelable>
        var resolution: Resolution = targetRouter.resolve(first() as Routing<Parcelable>)

        drop(1).forEach { element ->
            val bundles = emptyList<Bundle?>()

            // TODO don't build it again if already available as child.
            //  This probably means storing Node identifier in addition to (Parcelable) configuration.
            val ribs = buildStep(resolution)

            // TODO having 0 nodes is an impossible scenario, but having more than 1 can be valid.
            //  Solution is again to store Node identifiers & Bundles that help picking the correct one.
            val rib = ribs.first()

            rib.node.plugin<RoutingResolver<Parcelable>>()?.let {
                targetRouter = it
            } ?: throw IllegalStateException("Invalid chain of parents. This should never happen. Chain: $this")

            resolution = targetRouter.resolve(element as Routing<Parcelable>)
        }

        return resolution
    }

    private fun buildStep(resolution: Resolution): List<Rib> {
        return resolution.buildNodes(
            listOf(
                // we'll be discarding these Nodes, it doesn't matter
                BuildContext.root(
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
