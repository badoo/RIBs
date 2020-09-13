package com.badoo.ribs.sandbox.rib.compose_parent.routing

import android.os.Parcelable
import com.badoo.ribs.core.modality.BuildParams
import com.badoo.ribs.routing.Routing
import com.badoo.ribs.routing.action.AttachRibRoutingAction.Companion.attach
import com.badoo.ribs.routing.action.RoutingAction
import com.badoo.ribs.routing.router.Router
import com.badoo.ribs.routing.source.RoutingSource
import com.badoo.ribs.routing.transition.handler.TransitionHandler
import com.badoo.ribs.sandbox.rib.compose_parent.routing.ComposeParentRouter.Configuration
import com.badoo.ribs.sandbox.rib.compose_parent.routing.ComposeParentRouter.Configuration.Permanent.ComposeLeaf
import kotlinx.android.parcel.Parcelize

class ComposeParentRouter internal constructor(
    buildParams: BuildParams<*>,
    private val builders: ComposeParentChildBuilders,
    transitionHandler: TransitionHandler<Configuration>? = null
): Router<Configuration>(
    buildParams = buildParams,
    routingSource = RoutingSource.permanent(ComposeLeaf),
    transitionHandler = transitionHandler
) {
    sealed class Configuration : Parcelable {
        sealed class Permanent : Configuration() {
            @Parcelize object ComposeLeaf : Permanent()
        }
    }

    override fun resolve(routing: Routing<Configuration>): RoutingAction =
        with(builders) {
            when (routing.configuration) {
                is ComposeLeaf -> attach { composeLeaf.build(it) }
            }
        }
}

