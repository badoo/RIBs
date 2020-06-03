package com.badoo.ribs.template.node_dagger_build_param.foo_bar.routing

import android.os.Parcelable
import com.badoo.ribs.core.routing.router.Router
import com.badoo.ribs.core.builder.BuildParams
import com.badoo.ribs.core.routing.source.RoutingSource
import com.badoo.ribs.core.routing.action.RoutingAction
import com.badoo.ribs.core.routing.action.RoutingAction.Companion.noop
import com.badoo.ribs.core.routing.Routing
import com.badoo.ribs.core.routing.transition.handler.TransitionHandler
import com.badoo.ribs.template.node_dagger_build_param.foo_bar.routing.FooBarRouter.Configuration
import com.badoo.ribs.template.node_dagger_build_param.foo_bar.routing.FooBarRouter.Configuration.Content
import kotlinx.android.parcel.Parcelize

class FooBarRouter internal constructor(
    buildParams: BuildParams<*>,
    routingSource: RoutingSource<Configuration>,
    private val builders: FooBarChildBuilders,
    transitionHandler: TransitionHandler<Configuration>? = null
): Router<Configuration>(
    buildParams = buildParams,
    routingSource = routingSource,
    transitionHandler = transitionHandler
) {
    sealed class Configuration : Parcelable {
        sealed class Permanent : Configuration()
        sealed class Content : Configuration() {
            @Parcelize object Default : Content()
        }
        sealed class Overlay : Configuration()
    }

    override fun resolve(routing: Routing<Configuration>): RoutingAction =
        with(builders) {
            when (routing.configuration) {
                // TODO implement all branches
                //  to attach children use:
                //  Content.Child1 -> attach { child.build(it) }
                is Content.Default -> noop()
            }
        }
}
