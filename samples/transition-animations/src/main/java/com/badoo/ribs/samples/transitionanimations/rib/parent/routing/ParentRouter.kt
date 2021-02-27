package com.badoo.ribs.samples.transitionanimations.rib.parent.routing

import android.os.Parcelable
import com.badoo.ribs.core.modality.BuildParams
import com.badoo.ribs.routing.Routing
import com.badoo.ribs.routing.resolution.ChildResolution.Companion.child
import com.badoo.ribs.routing.resolution.Resolution
import com.badoo.ribs.routing.router.Router
import com.badoo.ribs.routing.source.RoutingSource
import com.badoo.ribs.routing.transition.handler.TransitionHandler
import com.badoo.ribs.samples.transitionanimations.rib.parent.routing.ParentRouter.Configuration
import com.badoo.ribs.samples.transitionanimations.rib.parent.routing.ParentRouter.Configuration.Child1
import com.badoo.ribs.samples.transitionanimations.rib.parent.routing.ParentRouter.Configuration.Child2
import kotlinx.android.parcel.Parcelize

class ParentRouter internal constructor(
        buildParams: BuildParams<Nothing?>,
        routingSource: RoutingSource<Configuration>,
        transitionHandler: TransitionHandler<Configuration>,
        private val builders: ParentChildBuilders
) : Router<Configuration>(
        buildParams = buildParams,
        routingSource = routingSource,
        transitionHandler = transitionHandler
) {

    sealed class Configuration : Parcelable {
        @Parcelize
        object Child1 : Configuration()
        @Parcelize
        object Child2 : Configuration()
    }

    override fun resolve(routing: Routing<Configuration>): Resolution =
            with(builders) {
                when (routing.configuration) {
                    Child1 -> child { child1Builder.build(it) }
                    Child2 -> child { child2Builder.build(it) }
                }
            }
}