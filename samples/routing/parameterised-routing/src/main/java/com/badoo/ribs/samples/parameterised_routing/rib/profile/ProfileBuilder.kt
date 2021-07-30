package com.badoo.ribs.samples.parameterised_routing.rib.profile

import com.badoo.ribs.builder.Builder
import com.badoo.ribs.core.modality.BuildParams

class ProfileBuilder : Builder<Profile.Params, Profile>() {

    override fun build(buildParams: BuildParams<Profile.Params>): Profile {
        val profileId: Int = buildParams.payload.profileId

        return ProfileNode(
            buildParams = buildParams,
            viewFactory = ProfileViewImpl.Factory(profileId).invoke(null),
            plugins = emptyList()
        )
    }
}
