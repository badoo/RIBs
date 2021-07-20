package com.badoo.ribs.example.root.routing

import android.os.Parcelable
import com.badoo.ribs.core.modality.BuildParams
import com.badoo.ribs.example.root.routing.RootRouter.Configuration
import com.badoo.ribs.example.root.routing.RootRouter.Configuration.Content
import com.badoo.ribs.routing.Routing
import com.badoo.ribs.routing.resolution.ChildResolution.Companion.child
import com.badoo.ribs.routing.resolution.Resolution
import com.badoo.ribs.routing.router.Router
import com.badoo.ribs.routing.source.RoutingSource
import com.badoo.ribs.routing.transition.handler.TransitionHandler
import kotlinx.parcelize.Parcelize

class RootRouter internal constructor(
    buildParams: BuildParams<*>,
    routingSource: RoutingSource<Configuration>,
    private val builders: RootChildBuilders,
    transitionHandler: TransitionHandler<Configuration>? = null
) : Router<Configuration>(
    buildParams = buildParams,
    routingSource = routingSource,
    transitionHandler = transitionHandler
) {
    sealed class Configuration : Parcelable {
        sealed class Content : Configuration() {
            @Parcelize
            object LoggedIn : Content()

            @Parcelize
            object LoggedOut : Content()

            @Parcelize
            object Login : Content()
        }
    }

    override fun resolve(routing: Routing<Configuration>): Resolution =
        with(builders) {
            when (routing.configuration) {
                is Content.LoggedIn -> child { loggedInContainerBuilder.build(it) }
                is Content.LoggedOut -> child { loggedOutContainerBuilder.build(it) }
                is Content.Login -> child { loginBuilder.build(it) }
            }
        }
}

