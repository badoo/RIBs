package com.badoo.ribs.samples.gallery.rib.communication_container.routing

import android.os.Parcelable
import com.badoo.ribs.core.modality.BuildParams
import com.badoo.ribs.routing.Routing
import com.badoo.ribs.routing.resolution.ChildResolution.Companion.child
import com.badoo.ribs.routing.resolution.Resolution
import com.badoo.ribs.routing.router.Router
import com.badoo.ribs.routing.source.RoutingSource
import com.badoo.ribs.routing.transition.handler.TransitionHandler
import com.badoo.ribs.samples.gallery.rib.communication_container.routing.CommunicationContainerRouter.Configuration
import kotlinx.android.parcel.Parcelize

class CommunicationContainerRouter internal constructor(
    buildParams: BuildParams<*>,
    routingSource: RoutingSource<Configuration>,
    private val builders: CommunicationContainerChildBuilders,
    transitionHandler: TransitionHandler<Configuration>? = null
): Router<Configuration>(
    buildParams = buildParams,
    routingSource = routingSource,
    transitionHandler = transitionHandler
) {
    sealed class Configuration : Parcelable {
        @Parcelize object Picker : Configuration()
        @Parcelize object MenuExample : Configuration()
        @Parcelize object MultiScreenExample : Configuration()
    }

    override fun resolve(routing: Routing<Configuration>): Resolution =
        with(builders) {
            when (routing.configuration) {
                Configuration.Picker -> child { picker.build(it) }
                Configuration.MenuExample -> TODO()
                Configuration.MultiScreenExample -> TODO()
            }
        }
}

