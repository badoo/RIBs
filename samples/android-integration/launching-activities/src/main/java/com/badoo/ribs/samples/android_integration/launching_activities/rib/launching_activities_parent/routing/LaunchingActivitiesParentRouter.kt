package com.badoo.ribs.samples.android_integration.launching_activities.rib.launching_activities_parent.routing

import android.os.Parcelable
import com.badoo.ribs.core.modality.BuildParams
import com.badoo.ribs.routing.Routing
import com.badoo.ribs.routing.resolution.ChildResolution.Companion.child
import com.badoo.ribs.routing.resolution.Resolution
import com.badoo.ribs.routing.router.Router
import com.badoo.ribs.routing.source.RoutingSource.Companion.permanent
import com.badoo.ribs.samples.android_integration.launching_activities.rib.launching_activities_parent.builder.LaunchingActivitiesChildBuilders
import kotlinx.android.parcel.Parcelize

class LaunchingActivitiesParentRouter(
        buildParams: BuildParams<*>,
        private val builders: LaunchingActivitiesChildBuilders
) : Router<LaunchingActivitiesParentRouter.Configuration>(
        buildParams = buildParams,
        routingSource = permanent(
                Configuration.Permanent.Child1,
                Configuration.Permanent.Child2,
        )
) {
    sealed class Configuration : Parcelable {
        sealed class Permanent : Configuration() {
            @Parcelize
            object Child1 : Configuration()

            @Parcelize
            object Child2 : Configuration()
        }
    }

    override fun resolve(routing: Routing<Configuration>): Resolution =
            with(builders) {
                when (routing.configuration) {
                    Configuration.Permanent.Child1 -> child { child1.build(it) }
                    Configuration.Permanent.Child2 -> child { child2.build(it) }
                }
            }
}
