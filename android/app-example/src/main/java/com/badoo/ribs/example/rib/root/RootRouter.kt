package com.badoo.ribs.example.rib.root

import android.os.Bundle
import android.os.Parcelable
import com.badoo.ribs.core.routing.portal.Portal
import com.badoo.ribs.core.routing.configuration.ConfigurationResolver
import com.badoo.ribs.core.Router
import com.badoo.ribs.core.routing.action.AttachRibRoutingAction.Companion.attach
import com.badoo.ribs.core.routing.action.RoutingAction
import com.badoo.ribs.core.view.RibView
import com.badoo.ribs.example.rib.root.RootRouter.Configuration
import com.badoo.ribs.example.rib.root.RootRouter.Configuration.Content
import com.badoo.ribs.example.rib.switcher.builder.SwitcherBuilder
import kotlinx.android.parcel.Parcelize

class RootRouter(
    savedInstanceState: Bundle?,
    private val switcherBuilder: SwitcherBuilder
): Router<Configuration, Nothing, Content, Nothing, Nothing>(
    savedInstanceState = savedInstanceState,
    initialConfiguration = Content.Default,
    permanentParts = emptyList()
), Portal {

    sealed class Configuration : Parcelable {
        sealed class Content : Configuration() {
            @Parcelize object Default : Content()
            @Parcelize data class Portal(val resolverChain: List<Parcelable>) : Content()
        }
    }

    override fun push(configurationChain: List<Parcelable>) {
        push(Content.Portal(configurationChain))
    }

    override fun resolveConfiguration(configuration: Configuration): RoutingAction<Nothing> =
        when (configuration) {
            is Content.Default -> attach { switcherBuilder.build(it) }
            is Content.Portal -> configuration.resolverChain.resolve() as RoutingAction<Nothing>
        }

    private fun <E : Parcelable> List<E>.resolve(): RoutingAction<out RibView> {
        // FIXME grab first from real root somehow (makeRoot method called by RibActivity?):
        var targetRouter: ConfigurationResolver<Parcelable, *> = this@RootRouter as ConfigurationResolver<Parcelable, *>
        var routingAction: RoutingAction<out RibView> = targetRouter.resolveConfiguration(first()) // FIXME make first() safe to call

        drop(1).forEach { element ->
            // FIXME don't build it again if already available as child
            val nodes = routingAction.buildNodes(emptyList()) // FIXME bundle
            val node = nodes.first() // FIXME if 0 or more than 1
            targetRouter = node.node.resolver as ConfigurationResolver<Parcelable, *>
            routingAction = targetRouter.resolveConfiguration(element)
        }

        return routingAction
    }

}

