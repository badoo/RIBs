package com.badoo.ribs.samples.android.launching_activities.rib.launching_activities_example.routing

import android.os.Parcelable
import com.badoo.ribs.core.modality.BuildParams
import com.badoo.ribs.routing.Routing
import com.badoo.ribs.routing.resolution.ChildResolution.Companion.child
import com.badoo.ribs.routing.resolution.Resolution
import com.badoo.ribs.routing.router.Router
import com.badoo.ribs.routing.source.RoutingSource.Companion.permanent
import com.badoo.ribs.samples.android.launching_activities.rib.launching_activities_example.routing.LaunchingActivitiesExampleRouter.Configuration.Permanent
import kotlinx.parcelize.Parcelize

class LaunchingActivitiesExampleRouter(
    buildParams: BuildParams<*>,
    private val builders: LaunchingActivitiesExampleChildBuilders
) : Router<LaunchingActivitiesExampleRouter.Configuration>(
    buildParams = buildParams,
    routingSource = permanent(
        Permanent.Child1,
        Permanent.Child2,
    )
) {
    sealed class Configuration : Parcelable {
        sealed class Permanent : Configuration() {
            @Parcelize
            data object Child1 : Configuration()

            @Parcelize
            data object Child2 : Configuration()
        }
    }

    override fun resolve(routing: Routing<Configuration>): Resolution =
        with(builders) {
            when (routing.configuration) {
                Permanent.Child1 -> child { child1.build(it) }
                Permanent.Child2 -> child { child2.build(it) }
            }
        }
}
