package com.badoo.ribs.example.component.app_bar.routing

import android.os.Parcelable
import com.badoo.ribs.core.modality.BuildParams
import com.badoo.ribs.example.component.app_bar.routing.AppBarRouter.Configuration
import com.badoo.ribs.example.component.app_bar.routing.AppBarRouter.Configuration.Content
import com.badoo.ribs.routing.Routing
import com.badoo.ribs.routing.action.RoutingAction
import com.badoo.ribs.routing.action.RoutingAction.Companion.noop
import com.badoo.ribs.routing.router.Router
import com.badoo.ribs.routing.source.RoutingSource
import com.badoo.ribs.routing.transition.handler.TransitionHandler
import kotlinx.android.parcel.Parcelize

class AppBarRouter internal constructor(
    buildParams: BuildParams<*>,
    routingSource: RoutingSource<Configuration>,
    private val builders: AppBarChildBuilders,
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

