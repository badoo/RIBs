package com.badoo.ribs.example.rib.root

import android.os.Bundle
import android.os.Parcelable
import com.badoo.ribs.core.Node
import com.badoo.ribs.core.Portal
import com.badoo.ribs.core.Resolver
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

    override fun push(resolverChain: List<Parcelable>) {
        push(Content.Portal(resolverChain))
    }

    override fun onSaveInstanceState(outState: Bundle) {
        newRoot(Content.Default)
        super.onSaveInstanceState(outState)
    }

    override fun resolveConfiguration(configuration: Configuration): RoutingAction<Nothing> =
        when (configuration) {
            is Content.Default -> attach { switcherBuilder.build(it) }
            is Content.Portal -> configuration.resolverChain.resolve() as RoutingAction<Nothing>
        }

    private fun <E : Parcelable> List<E>.resolve(): RoutingAction<out RibView> {
        // FIXME grab first from real root somehow (makeRoot method called by RibActivity?):
        var targetRouter: Resolver<Parcelable, *> = this@RootRouter as Resolver<Parcelable, *>
        var routingAction: RoutingAction<out RibView> = targetRouter.resolveConfiguration(Content.Default) // FIXME this is set to Content.Default

        drop(1).forEach { element ->
            // FIXME don't build it again if already available as child
            val nodes = routingAction.buildNodes(emptyList()) // FIXME bundle
            val node = nodes.first() // FIXME if 0 or more than 1
            targetRouter = node.node.resolver as Resolver<Parcelable, *>
            routingAction = targetRouter.resolveConfiguration(element)
        }

        return routingAction
    }

}

