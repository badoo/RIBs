package com.badoo.ribs.samples.android.launching_activities.rib.launching_activities_example

import com.badoo.ribs.android.activitystarter.ActivityStarter
import com.badoo.ribs.builder.SimpleBuilder
import com.badoo.ribs.core.modality.BuildParams
import com.badoo.ribs.samples.android.launching_activities.R
import com.badoo.ribs.samples.android.launching_activities.rib.child_common.LaunchingActivitiesChildBase
import com.badoo.ribs.samples.android.launching_activities.rib.launching_activities_example.routing.LaunchingActivitiesExampleChildBuilders
import com.badoo.ribs.samples.android.launching_activities.rib.launching_activities_example.routing.LaunchingActivitiesExampleRouter

class LaunchingActivitiesExampleBuilder(
    private val dependency: LaunchingActivitiesExample.Dependency
) : SimpleBuilder<LaunchingActivitiesExample>() {
    override fun build(buildParams: BuildParams<Nothing?>): LaunchingActivitiesExample {
        val router = LaunchingActivitiesExampleRouter(
            buildParams = buildParams,
            builders = LaunchingActivitiesExampleChildBuilders(object : LaunchingActivitiesChildBase.Dependency {
                override val activityStarter: ActivityStarter = dependency.activityStarter
            }),
        )
        val viewDependency = object : LaunchingActivitiesExampleView.Dependency {
            override fun getTitleResource(): Int = R.string.parent_title
            override fun getDescriptionResource(): Int = R.string.parent_description
        }

        return LaunchingActivitiesExampleNode(
            buildParams = buildParams,
            viewFactory = LaunchingActivitiesExampleViewImpl.Factory().invoke(viewDependency),
            plugins = listOf(router),
        )
    }
}
