package com.badoo.ribs.samples.android.launching_activities.rib.child1

import com.badoo.ribs.builder.SimpleBuilder
import com.badoo.ribs.core.modality.BuildParams
import com.badoo.ribs.samples.android.launching_activities.R
import com.badoo.ribs.samples.android.launching_activities.rib.child_common.LaunchingActivitiesChildBase
import com.badoo.ribs.samples.android.launching_activities.rib.child_common.LaunchingActivitiesChildInteractor
import com.badoo.ribs.samples.android.launching_activities.rib.child_common.LaunchingActivitiesChildView
import com.badoo.ribs.samples.android.launching_activities.rib.child_common.LaunchingActivitiesChildViewImpl

class LaunchingActivitiesChild1Builder(
    private val dependency: LaunchingActivitiesChildBase.Dependency
) : SimpleBuilder<LaunchingActivitiesChild1>() {
    override fun build(buildParams: BuildParams<Nothing?>): LaunchingActivitiesChild1 {
        val interactor = LaunchingActivitiesChildInteractor(
            buildParams = buildParams,
            activityStarter = dependency.activityStarter
        )
        val viewDependency = object : LaunchingActivitiesChildView.Dependency {
            override val titleResource: Int = R.string.child1_title
            override val descriptionResource: Int = R.string.child1_description
        }

        return LaunchingActivitiesChild1Node(
            buildParams = buildParams,
            viewFactory = LaunchingActivitiesChildViewImpl.Factory().invoke(viewDependency),
            plugins = listOf(interactor)
        )
    }
}
