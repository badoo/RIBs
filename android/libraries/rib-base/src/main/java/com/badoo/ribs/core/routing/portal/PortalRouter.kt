package com.badoo.ribs.core.routing.portal

import android.os.Bundle
import android.os.Parcelable
import com.badoo.ribs.core.Router
import com.badoo.ribs.core.routing.action.RoutingAction
import com.badoo.ribs.core.routing.configuration.ConfigurationResolver
import com.badoo.ribs.core.routing.portal.PortalRouter.Configuration
import com.badoo.ribs.core.routing.portal.PortalRouter.Configuration.Content
import com.badoo.ribs.core.view.RibView
import kotlinx.android.parcel.Parcelize

class PortalRouter(
    savedInstanceState: Bundle?
): Router<Configuration, Nothing, Content, Nothing, Nothing>(
    savedInstanceState = savedInstanceState,
    initialConfiguration = Content.Default,
    permanentParts = emptyList()
), Portal.OtherSide {

    internal lateinit var defaultRoutingAction: RoutingAction<Nothing>

    sealed class Configuration : Parcelable {
        sealed class Content : Configuration() {
            @Parcelize object Default : Content()
            @Parcelize data class Portal(val configurationChain: List<Parcelable>) : Content()
        }
    }

    override fun showRemote(remoteRouter: Router<*, *, *, *, *>, remoteConfiguration: Parcelable) {
        push(Content.Portal(remoteRouter.node.ancestryInfo.configurationChain + remoteConfiguration))
    }

    override fun resolveConfiguration(configuration: Configuration): RoutingAction<Nothing> =
        when (configuration) {
            is Content.Default -> defaultRoutingAction
            is Content.Portal -> configuration.configurationChain.resolve() as RoutingAction<Nothing>
        }

    private fun List<Parcelable>.resolve(): RoutingAction<out RibView> {
        // TODO grab first from real root somehow -- currently works only if PortalRouter is in the root rib
        var targetRouter: ConfigurationResolver<Parcelable, *> =
            this@PortalRouter as ConfigurationResolver<Parcelable, *>
        var routingAction: RoutingAction<out RibView> =
            targetRouter.resolveConfiguration(first())

        drop(1).forEach { element ->
            // TODO don't build it again if already available as child
            val nodes = routingAction.buildNodes(emptyList()) // TODO add bundle
            val node = nodes.first() // TODO handle if 0 or more than 1
            targetRouter = node.node.resolver as ConfigurationResolver<Parcelable, *>
            routingAction = targetRouter.resolveConfiguration(element)
        }

        return routingAction
    }
}
