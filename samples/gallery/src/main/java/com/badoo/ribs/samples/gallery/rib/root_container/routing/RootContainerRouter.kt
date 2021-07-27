package com.badoo.ribs.samples.gallery.rib.root_container.routing

import android.os.Parcelable
import com.badoo.ribs.core.modality.BuildParams
import com.badoo.ribs.routing.Routing
import com.badoo.ribs.routing.resolution.ChildResolution.Companion.child
import com.badoo.ribs.routing.resolution.Resolution
import com.badoo.ribs.routing.router.Router
import com.badoo.ribs.routing.source.RoutingSource
import com.badoo.ribs.routing.transition.handler.TransitionHandler
import com.badoo.ribs.samples.gallery.rib.root_container.routing.RootContainerRouter.Configuration
import kotlinx.parcelize.Parcelize

class RootContainerRouter internal constructor(
    buildParams: BuildParams<*>,
    routingSource: RoutingSource<Configuration>,
    private val builders: RootContainerChildBuilders,
    transitionHandler: TransitionHandler<Configuration>? = null
): Router<Configuration>(
    buildParams = buildParams,
    routingSource = routingSource,
    transitionHandler = transitionHandler
) {
    sealed class Configuration : Parcelable {
        @Parcelize object Picker : Configuration()
    }

    override fun resolve(routing: Routing<Configuration>): Resolution =
        with(builders) {
            when (routing.configuration) {
                is Configuration.Picker -> child { picker.build(it) }
            }
        }
}

