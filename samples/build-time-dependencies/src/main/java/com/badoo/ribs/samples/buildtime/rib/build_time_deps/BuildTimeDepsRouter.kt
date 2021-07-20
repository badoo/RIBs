package com.badoo.ribs.samples.buildtime.rib.build_time_deps

import android.os.Parcelable
import com.badoo.ribs.core.modality.BuildParams
import com.badoo.ribs.routing.Routing
import com.badoo.ribs.routing.resolution.ChildResolution.Companion.child
import com.badoo.ribs.routing.resolution.Resolution
import com.badoo.ribs.routing.resolution.Resolution.Companion.noop
import com.badoo.ribs.routing.router.Router
import com.badoo.ribs.routing.source.RoutingSource
import com.badoo.ribs.samples.buildtime.rib.build_time_deps.BuildTimeDepsRouter.Configuration
import com.badoo.ribs.samples.buildtime.rib.build_time_deps.BuildTimeDepsRouter.Configuration.Default
import com.badoo.ribs.samples.buildtime.rib.build_time_deps.BuildTimeDepsRouter.Configuration.ShowProfile
import com.badoo.ribs.samples.buildtime.rib.profile.Profile
import com.badoo.ribs.samples.buildtime.rib.profile.ProfileBuilder
import kotlinx.parcelize.Parcelize

internal class BuildTimeDepsRouter(
    buildParams: BuildParams<*>,
    routingSource: RoutingSource<Configuration>,
    private val profileBuilder: ProfileBuilder
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
            is Default -> noop()
            is ShowProfile -> child {
                profileBuilder.build(it, Profile.Params(configuration.profileId))
            }
        }
}
