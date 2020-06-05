package com.badoo.ribs.template.node.foo_bar.routing

import android.os.Parcelable
import com.badoo.ribs.core.Router
import com.badoo.ribs.core.builder.BuildParams
import com.badoo.ribs.core.routing.action.RoutingAction
import com.badoo.ribs.core.routing.transition.handler.TransitionHandler
import com.badoo.ribs.template.node.foo_bar.FooBarView
import com.badoo.ribs.template.node.foo_bar.routing.FooBarRouter.Configuration
import com.badoo.ribs.template.node.foo_bar.routing.FooBarRouter.Configuration.Content
import com.badoo.ribs.template.node.foo_bar.routing.FooBarRouter.Configuration.Overlay
import com.badoo.ribs.template.node.foo_bar.routing.FooBarRouter.Configuration.Permanent
import kotlinx.android.parcel.Parcelize
class FooBarRouter internal constructor(
    buildParams: BuildParams<*>,
    private val connections: FooBarConnections,
    transitionHandler: TransitionHandler<Configuration>? = null
): Router<Configuration, Permanent, Content, Overlay, FooBarView>(
    buildParams = buildParams,
    transitionHandler = transitionHandler,
    initialConfiguration = Content.Default,
    permanentParts = emptyList()
) {
    sealed class Configuration : Parcelable {
        sealed class Permanent : Configuration()
        sealed class Content : Configuration() {
            @Parcelize object Default : Content()
        }
        sealed class Overlay : Configuration()
    }

    override fun resolveConfiguration(configuration: Configuration): RoutingAction =
        with(connections) {
            when (configuration) {
                // TODO implement all branches
                //  to attach children use:
                //  Content.Child1 -> attach { connections.child.build(it) }
                Content.Default -> RoutingAction.noop()
            }
        }
}

