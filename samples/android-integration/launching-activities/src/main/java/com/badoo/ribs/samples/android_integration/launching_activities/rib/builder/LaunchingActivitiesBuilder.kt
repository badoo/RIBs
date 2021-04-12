package com.badoo.ribs.samples.android_integration.launching_activities.rib.builder

import com.badoo.ribs.builder.SimpleBuilder
import com.badoo.ribs.core.modality.BuildParams
import com.badoo.ribs.samples.android_integration.launching_activities.rib.LaunchingActivities
import com.badoo.ribs.samples.android_integration.launching_activities.rib.LaunchingActivitiesInteractor
import com.badoo.ribs.samples.android_integration.launching_activities.rib.LaunchingActivitiesNode
import com.badoo.ribs.samples.android_integration.launching_activities.rib.LaunchingActivitiesViewImpl

class LaunchingActivitiesBuilder(
        private val dependency: LaunchingActivities.Dependency
) : SimpleBuilder<LaunchingActivities>() {
    override fun build(buildParams: BuildParams<Nothing?>): LaunchingActivities {
        val interactor = LaunchingActivitiesInteractor(
                buildParams = buildParams,
                activityStarter = dependency.activityStarter
        )
        return LaunchingActivitiesNode(
                buildParams = buildParams,
                viewFactory = LaunchingActivitiesViewImpl.Factory().invoke(null),
                plugins = listOf(interactor)
        )
    }
}
