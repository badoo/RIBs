package com.badoo.ribs.samples.android_integration.launching_activities.rib.parent.builder

import com.badoo.ribs.android.activitystarter.ActivityStarter
import com.badoo.ribs.builder.SimpleBuilder
import com.badoo.ribs.core.modality.BuildParams
import com.badoo.ribs.samples.android_integration.launching_activities.R
import com.badoo.ribs.samples.android_integration.launching_activities.rib.child_common.LaunchingActivitiesChildBase
import com.badoo.ribs.samples.android_integration.launching_activities.rib.parent.LaunchingActivitiesParent
import com.badoo.ribs.samples.android_integration.launching_activities.rib.parent.LaunchingActivitiesParentNode
import com.badoo.ribs.samples.android_integration.launching_activities.rib.parent.LaunchingActivitiesParentRouter
import com.badoo.ribs.samples.android_integration.launching_activities.rib.parent.LaunchingActivitiesParentView
import com.badoo.ribs.samples.android_integration.launching_activities.rib.parent.LaunchingActivitiesParentViewImpl

class LaunchingActivitiesParentBuilder(
    private val dependency: LaunchingActivitiesParent.Dependency
) : SimpleBuilder<LaunchingActivitiesParent>() {
    override fun build(buildParams: BuildParams<Nothing?>): LaunchingActivitiesParent {
        val router = LaunchingActivitiesParentRouter(
            buildParams = buildParams,
            builders = LaunchingActivitiesChildBuilders(object : LaunchingActivitiesChildBase.Dependency {
                override val activityStarter: ActivityStarter = dependency.activityStarter
            }),
        )
        val viewDependency = object : LaunchingActivitiesParentView.Dependency {
            override fun getTitleResource(): Int = R.string.parent_title
            override fun getDescriptionResource(): Int = R.string.parent_description
        }

        return LaunchingActivitiesParentNode(
            buildParams = buildParams,
            viewFactory = LaunchingActivitiesParentViewImpl.Factory().invoke(viewDependency),
            plugins = listOf(router),
        )
    }
}
