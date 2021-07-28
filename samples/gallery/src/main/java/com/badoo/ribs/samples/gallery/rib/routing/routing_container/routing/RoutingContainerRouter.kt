package com.badoo.ribs.samples.gallery.rib.routing.routing_container.routing

import android.os.Parcelable
import com.badoo.ribs.core.modality.BuildParams
import com.badoo.ribs.routing.Routing
import com.badoo.ribs.routing.resolution.ChildResolution.Companion.child
import com.badoo.ribs.routing.resolution.Resolution
import com.badoo.ribs.routing.router.Router
import com.badoo.ribs.routing.source.RoutingSource
import com.badoo.ribs.routing.transition.handler.TransitionHandler
import com.badoo.ribs.samples.gallery.rib.routing.routing_container.routing.RoutingContainerRouter.Configuration
import kotlinx.parcelize.Parcelize

class RoutingContainerRouter internal constructor(
    buildParams: BuildParams<*>,
    routingSource: RoutingSource<Configuration>,
    private val builders: RoutingContainerChildBuilders,
    transitionHandler: TransitionHandler<Configuration>? = null
): Router<Configuration>(
    buildParams = buildParams,
    routingSource = routingSource,
    transitionHandler = transitionHandler
) {
    sealed class Configuration : Parcelable {
        @Parcelize object Picker : Configuration()
        @Parcelize object SimpleRoutingExample : Configuration()
        @Parcelize object BackStackExample : Configuration()
        @Parcelize object TransitionsExample : Configuration()
    }

    override fun resolve(routing: Routing<Configuration>): Resolution =
        with(builders) {
            when (routing.configuration) {
                Configuration.Picker -> child { picker.build(it) }
                Configuration.SimpleRoutingExample -> child { simpleRouting.build(it) }
                Configuration.BackStackExample -> child { backStackExample.build(it) }
                Configuration.TransitionsExample -> TODO()
            }
        }
}

