package com.badoo.ribs.core.routing.portal

import android.os.Bundle
import android.os.Parcelable
import com.badoo.ribs.core.Router
import com.badoo.ribs.core.builder.BuildContext
import com.badoo.ribs.core.builder.BuildParams
import com.badoo.ribs.core.routing.action.RoutingAction
import com.badoo.ribs.core.routing.configuration.ConfigurationResolver
import com.badoo.ribs.core.routing.configuration.feature.operation.push
import com.badoo.ribs.core.routing.configuration.feature.operation.pushOverlay
import com.badoo.ribs.core.routing.portal.PortalRouter.Configuration
import com.badoo.ribs.core.routing.portal.PortalRouter.Configuration.Content
import com.badoo.ribs.core.routing.portal.PortalRouter.Configuration.Overlay
import com.badoo.ribs.core.routing.transition.handler.TransitionHandler
import kotlinx.android.parcel.Parcelize

class PortalRouter(
    buildParams: BuildParams<*>,
    transitionHandler: TransitionHandler<Configuration>? = null
): Router<Configuration, Nothing, Content, Overlay, Nothing>(
    buildParams = buildParams,
    transitionHandler = transitionHandler,
    initialConfiguration = Content.Default,
    permanentParts = emptyList()
), Portal.OtherSide {

    internal lateinit var defaultRoutingAction: RoutingAction

    sealed class Configuration : Parcelable {
        sealed class Content : Configuration() {
            @Parcelize object Default : Content()
            @Parcelize data class Portal(val configurationChain: List<Parcelable>) : Content()
        }
        sealed class Overlay : Configuration() {
            @Parcelize data class Portal(val configurationChain: List<Parcelable>) : Overlay()
        }
    }

    override fun showContent(remoteRouter: Router<*, *, *, *, *>, remoteConfiguration: Parcelable) {
        push(Content.Portal(remoteRouter.node.ancestryInfo.configurationChain + remoteConfiguration))
    }

    override fun showOverlay(remoteRouter: Router<*, *, *, *, *>, remoteConfiguration: Parcelable) {
        pushOverlay(Overlay.Portal(remoteRouter.node.ancestryInfo.configurationChain + remoteConfiguration))
    }

    override fun resolveConfiguration(configuration: Configuration): RoutingAction =
        when (configuration) {
            is Content.Default -> defaultRoutingAction
            is Content.Portal -> configuration.configurationChain.resolve()
            is Overlay.Portal -> configuration.configurationChain.resolve()
        }

    // TODO probably needs to change from List<Parcelable> to List<AncestryInfo>,
    //  so that extra info can be added too. See below for details.
    private fun List<Parcelable>.resolve(): RoutingAction {
        // TODO grab first from real root somehow -- currently works only if PortalRouter is in the root rib
        var targetRouter: ConfigurationResolver<Parcelable> =
            this@PortalRouter as ConfigurationResolver<Parcelable>
        var routingAction: RoutingAction =
            targetRouter.resolveConfiguration(first())

        drop(1).forEach { element ->
            val bundles = emptyList<Bundle?>()

            // TODO don't build it again if already available as child.
            //  This probably means storing Node identifier in addition to (Parcelable) configuration.
            val nodes = routingAction.buildNodes(
                listOf(
                    BuildContext(
                        ancestryInfo = AncestryInfo.Root, // we'll be discarding these Nodes, it doesn't matter
                        // TODO for maximum correctness, original List<> should also contain Bundles,
                        //  as that might change how dependencies are built (right now there's no case for this,
                        //  but can be in the future).
                        savedInstanceState = null
                    )
                )
            )

            // TODO having 0 nodes is an impossible scenario, but having more than 1 can be valid.
            //  Solution is again to store Node identifiers & Bundles that help picking the correct one.
            val node = nodes.first()
            targetRouter = node.resolver as ConfigurationResolver<Parcelable>
            routingAction = targetRouter.resolveConfiguration(element)
        }

        return routingAction
    }
}
