package com.badoo.ribs.samples.android_integration.launching_activities.rib.launching_activities_child2.builder

import com.badoo.ribs.builder.SimpleBuilder
import com.badoo.ribs.core.modality.BuildParams
import com.badoo.ribs.samples.android_integration.launching_activities.R
import com.badoo.ribs.samples.android_integration.launching_activities.rib.launching_activities_child2.LaunchingActivitiesChild2
import com.badoo.ribs.samples.android_integration.launching_activities.rib.launching_activities_child2.LaunchingActivitiesChild2Node
import com.badoo.ribs.samples.android_integration.launching_activities.rib.launching_activities_child_common.LaunchingActivitiesChildBase
import com.badoo.ribs.samples.android_integration.launching_activities.rib.launching_activities_child_common.LaunchingActivitiesChildInteractor
import com.badoo.ribs.samples.android_integration.launching_activities.rib.launching_activities_child_common.LaunchingActivitiesChildView
import com.badoo.ribs.samples.android_integration.launching_activities.rib.launching_activities_child_common.LaunchingActivitiesChildViewImpl

class LaunchingActivitiesChild2Builder(
        private val dependency: LaunchingActivitiesChildBase.Dependency
) : SimpleBuilder<LaunchingActivitiesChild2>() {
    override fun build(buildParams: BuildParams<Nothing?>): LaunchingActivitiesChild2 {
        val interactor = LaunchingActivitiesChildInteractor(
                buildParams = buildParams,
                activityStarter = dependency.activityStarter
        )
        val viewDependency = object : LaunchingActivitiesChildView.Dependency {
            override fun getTitleResource(): Int = R.string.child2_title
            override fun getDescriptionResource(): Int = R.string.child2_description
        }

        return LaunchingActivitiesChild2Node(
                buildParams = buildParams,
                viewFactory = LaunchingActivitiesChildViewImpl.Factory().invoke(viewDependency),
                plugins = listOf(interactor)
        )
    }
}
