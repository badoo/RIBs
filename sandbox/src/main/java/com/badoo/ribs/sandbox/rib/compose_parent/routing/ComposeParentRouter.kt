package com.badoo.ribs.sandbox.rib.compose_parent.routing

import android.os.Parcelable
import com.badoo.ribs.core.modality.BuildParams
import com.badoo.ribs.routing.Routing
import com.badoo.ribs.routing.resolution.ChildResolution.Companion.child
import com.badoo.ribs.routing.resolution.Resolution
import com.badoo.ribs.routing.router.Router
import com.badoo.ribs.routing.source.RoutingSource
import com.badoo.ribs.routing.transition.handler.TransitionHandler
import com.badoo.ribs.sandbox.rib.compose_leaf.ComposeLeaf
import com.badoo.ribs.sandbox.rib.compose_parent.routing.ComposeParentRouter.Configuration
import com.badoo.ribs.sandbox.rib.compose_parent.routing.ComposeParentRouter.Configuration.Content
import kotlinx.parcelize.Parcelize

class ComposeParentRouter internal constructor(
    buildParams: BuildParams<*>,
    routingSource: RoutingSource<Configuration>,
    private val builders: ComposeParentChildBuilders,
    transitionHandler: TransitionHandler<Configuration>? = null
) : Router<Configuration>(
    buildParams = buildParams,
    routingSource = routingSource,
    transitionHandler = transitionHandler
) {
    sealed class Configuration : Parcelable {
        sealed class Content : Configuration() {
            @Parcelize
            data class ComposeLeaf(val i: Int) : Content()
        }
    }

    override fun resolve(routing: Routing<Configuration>): Resolution =
        with(builders) {
            when (val configuration = routing.configuration) {
                is Content.ComposeLeaf -> child {
                    composeLeaf.build(
                        it,
                        ComposeLeaf.Params(configuration.i)
                    )
                }
            }
        }
}

