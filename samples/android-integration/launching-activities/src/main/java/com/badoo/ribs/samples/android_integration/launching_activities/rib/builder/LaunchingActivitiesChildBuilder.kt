package com.badoo.ribs.samples.android_integration.launching_activities.rib.builder

import com.badoo.ribs.builder.SimpleBuilder
import com.badoo.ribs.core.modality.BuildParams
import com.badoo.ribs.samples.android_integration.launching_activities.R
import com.badoo.ribs.samples.android_integration.launching_activities.rib.LaunchingActivities
import com.badoo.ribs.samples.android_integration.launching_activities.rib.LaunchingActivitiesInteractor
import com.badoo.ribs.samples.android_integration.launching_activities.rib.LaunchingActivitiesNode
import com.badoo.ribs.samples.android_integration.launching_activities.rib.LaunchingActivitiesView
import com.badoo.ribs.samples.android_integration.launching_activities.rib.LaunchingActivitiesViewImpl

class LaunchingActivitiesChildBuilder(
        private val dependency: LaunchingActivities.Dependency
) : SimpleBuilder<LaunchingActivities>() {
    override fun build(buildParams: BuildParams<Nothing?>): LaunchingActivities {
        val interactor = LaunchingActivitiesInteractor(
                buildParams = buildParams,
                activityStarter = dependency.activityStarter
        )
        val viewDependency = object : LaunchingActivitiesView.Dependency {
            override fun getTitleResource(): Int = R.string.child_title
            override fun getDescriptionResource(): Int = R.string.child_description
        }

        return LaunchingActivitiesNode(
                buildParams = buildParams,
                viewFactory = LaunchingActivitiesViewImpl.Factory(layoutRes = R.layout.rib_launching_activities).invoke(viewDependency),
                plugins = listOf(interactor)
        )
    }
}
