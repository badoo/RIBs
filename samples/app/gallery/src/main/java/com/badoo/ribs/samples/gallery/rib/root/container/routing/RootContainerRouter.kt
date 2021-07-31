package com.badoo.ribs.samples.gallery.rib.root.container.routing

import android.os.Parcelable
import com.badoo.ribs.core.modality.BuildParams
import com.badoo.ribs.routing.Routing
import com.badoo.ribs.routing.resolution.ChildResolution.Companion.child
import com.badoo.ribs.routing.resolution.Resolution
import com.badoo.ribs.routing.router.Router
import com.badoo.ribs.routing.source.RoutingSource
import com.badoo.ribs.routing.transition.handler.TransitionHandler
import com.badoo.ribs.samples.gallery.rib.root.container.routing.RootContainerRouter.Configuration
import com.badoo.ribs.samples.gallery.rib.root.container.routing.RootContainerRouter.Configuration.*
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
        @Parcelize object RoutingExamples : Configuration()
        @Parcelize object CommunicationExamples : Configuration()
        @Parcelize object AndroidExamples : Configuration()
        @Parcelize object OtherExamples : Configuration()
    }

    override fun resolve(routing: Routing<Configuration>): Resolution =
        with(builders) {
            when (routing.configuration) {
                is Picker -> child { picker.build(it) }
                is CommunicationExamples -> child { communicationContainer.build(it) }
                is AndroidExamples -> child { androidContainer.build(it) }
                is RoutingExamples -> child { routingContainer.build(it) }
                is OtherExamples -> child { otherContainer.build(it) }
            }
        }
}

