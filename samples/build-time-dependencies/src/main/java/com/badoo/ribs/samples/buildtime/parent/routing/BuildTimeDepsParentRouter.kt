package com.badoo.ribs.samples.buildtime.parent.routing

import android.os.Parcelable
import com.badoo.ribs.core.modality.BuildParams
import com.badoo.ribs.routing.Routing
import com.badoo.ribs.routing.resolution.ChildResolution.Companion.child
import com.badoo.ribs.routing.resolution.Resolution
import com.badoo.ribs.routing.router.Router
import com.badoo.ribs.routing.source.RoutingSource
import com.badoo.ribs.samples.buildtime.parent.routing.BuildTimeDepsParentRouter.Configuration
import com.badoo.ribs.samples.buildtime.parent.routing.BuildTimeDepsParentRouter.Configuration.Default
import com.badoo.ribs.samples.buildtime.parent.routing.BuildTimeDepsParentRouter.Configuration.ShowProfile
import com.badoo.ribs.samples.buildtime.profile.BuildTimeDepsProfile
import com.badoo.ribs.samples.buildtime.profile.builder.BuildTimeDepsProfileBuilder
import kotlinx.android.parcel.Parcelize

internal class BuildTimeDepsParentRouter(
    buildParams: BuildParams<*>,
    routingSource: RoutingSource<Configuration>,
    private val profileBuilder: BuildTimeDepsProfileBuilder
) : Router<Configuration>(
    buildParams = buildParams,
    routingSource = routingSource
) {

    sealed class Configuration : Parcelable {
        @Parcelize
        object Default : Configuration()

        @Parcelize
        data class ShowProfile(
            val profileId: Int
        ) : Configuration()
    }

    override fun resolve(routing: Routing<Configuration>): Resolution =
        when (val configuration = routing.configuration) {
            is Default -> Resolution.noop()
            is ShowProfile -> child {
                profileBuilder.build(it, BuildTimeDepsProfile.Params(configuration.profileId))
            }
        }
}
