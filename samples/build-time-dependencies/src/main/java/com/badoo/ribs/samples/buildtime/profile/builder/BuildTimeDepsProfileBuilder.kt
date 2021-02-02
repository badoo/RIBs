package com.badoo.ribs.samples.buildtime.profile.builder

import com.badoo.ribs.builder.Builder
import com.badoo.ribs.core.modality.BuildParams
import com.badoo.ribs.samples.buildtime.profile.BuildTimeDepsProfile
import com.badoo.ribs.samples.buildtime.profile.BuildTimeDepsProfileNode
import com.badoo.ribs.samples.buildtime.profile.BuildTimeDepsProfileViewImpl

class BuildTimeDepsProfileBuilder : Builder<BuildTimeDepsProfile.Params, BuildTimeDepsProfile>() {

    override fun build(buildParams: BuildParams<BuildTimeDepsProfile.Params>): BuildTimeDepsProfile {
        val profileId: Int = buildParams.payload.profileId

        return BuildTimeDepsProfileNode(
            buildParams = buildParams,
            viewFactory = BuildTimeDepsProfileViewImpl.Factory(profileId).invoke(null),
            plugins = emptyList()
        )
    }
}
