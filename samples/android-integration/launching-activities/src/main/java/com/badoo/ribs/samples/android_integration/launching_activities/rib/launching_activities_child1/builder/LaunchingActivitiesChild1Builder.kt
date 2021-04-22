package com.badoo.ribs.samples.android_integration.launching_activities.rib.launching_activities_child1.builder

import com.badoo.ribs.builder.SimpleBuilder
import com.badoo.ribs.core.modality.BuildParams
import com.badoo.ribs.samples.android_integration.launching_activities.R
import com.badoo.ribs.samples.android_integration.launching_activities.rib.launching_activities_child1.LaunchingActivitiesChild1
import com.badoo.ribs.samples.android_integration.launching_activities.rib.launching_activities_child1.LaunchingActivitiesChild1Node
import com.badoo.ribs.samples.android_integration.launching_activities.rib.launching_activities_child_common.LaunchingActivitiesChildBase
import com.badoo.ribs.samples.android_integration.launching_activities.rib.launching_activities_child_common.LaunchingActivitiesChildInteractor
import com.badoo.ribs.samples.android_integration.launching_activities.rib.launching_activities_child_common.LaunchingActivitiesChildView
import com.badoo.ribs.samples.android_integration.launching_activities.rib.launching_activities_child_common.LaunchingActivitiesChildViewImpl

class LaunchingActivitiesChild1Builder(
        private val dependency: LaunchingActivitiesChildBase.Dependency
) : SimpleBuilder<LaunchingActivitiesChild1>() {
    override fun build(buildParams: BuildParams<Nothing?>): LaunchingActivitiesChild1 {
        val interactor = LaunchingActivitiesChildInteractor(
                buildParams = buildParams,
                activityStarter = dependency.activityStarter
        )
        val viewDependency = object : LaunchingActivitiesChildView.Dependency {
            override fun getTitleResource(): Int = R.string.child1_title
            override fun getDescriptionResource(): Int = R.string.child1_description
        }

        return LaunchingActivitiesChild1Node(
                buildParams = buildParams,
                viewFactory = LaunchingActivitiesChildViewImpl.Factory().invoke(viewDependency),
                plugins = listOf(interactor)
        )
    }
}
