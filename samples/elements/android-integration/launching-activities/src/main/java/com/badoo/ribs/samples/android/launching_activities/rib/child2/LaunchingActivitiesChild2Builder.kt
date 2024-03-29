package com.badoo.ribs.samples.android.launching_activities.rib.child2

import com.badoo.ribs.builder.SimpleBuilder
import com.badoo.ribs.core.modality.BuildParams
import com.badoo.ribs.samples.android.launching_activities.R
import com.badoo.ribs.samples.android.launching_activities.rib.child_common.LaunchingActivitiesChildBase
import com.badoo.ribs.samples.android.launching_activities.rib.child_common.LaunchingActivitiesChildInteractor
import com.badoo.ribs.samples.android.launching_activities.rib.child_common.LaunchingActivitiesChildView
import com.badoo.ribs.samples.android.launching_activities.rib.child_common.LaunchingActivitiesChildViewImpl

class LaunchingActivitiesChild2Builder(
    private val dependency: LaunchingActivitiesChildBase.Dependency
) : SimpleBuilder<LaunchingActivitiesChild2>() {
    override fun build(buildParams: BuildParams<Nothing?>): LaunchingActivitiesChild2 {
        val interactor = LaunchingActivitiesChildInteractor(
            buildParams = buildParams,
            activityStarter = dependency.activityStarter
        )
        val viewDependency = object : LaunchingActivitiesChildView.Dependency {
            override val titleResource: Int = R.string.child2_title
            override val descriptionResource: Int = R.string.child2_description
        }

        return LaunchingActivitiesChild2Node(
            buildParams = buildParams,
            viewFactory = LaunchingActivitiesChildViewImpl.Factory().invoke(viewDependency),
            plugins = listOf(interactor)
        )
    }
}
