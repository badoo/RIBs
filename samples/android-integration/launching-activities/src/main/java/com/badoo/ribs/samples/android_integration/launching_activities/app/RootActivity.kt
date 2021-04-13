package com.badoo.ribs.samples.android_integration.launching_activities.app

import android.os.Bundle
import android.view.ViewGroup
import com.badoo.ribs.android.RibActivity
import com.badoo.ribs.android.activitystarter.ActivityStarter
import com.badoo.ribs.core.Rib
import com.badoo.ribs.core.modality.BuildContext.Companion.root
import com.badoo.ribs.samples.android_integration.launching_activities.R
import com.badoo.ribs.samples.android_integration.launching_activities.rib.LaunchingActivities
import com.badoo.ribs.samples.android_integration.launching_activities.rib.builder.LaunchingActivitiesParentBuilder

class RootActivity : RibActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        setContentView(R.layout.activity_root)
        super.onCreate(savedInstanceState)
    }

    override val rootViewGroup: ViewGroup
        get() = findViewById(R.id.root)

    override fun createRib(savedInstanceState: Bundle?): Rib =
            LaunchingActivitiesParentBuilder(object : LaunchingActivities.Dependency {
                override val activityStarter: ActivityStarter = integrationPoint.activityStarter
            }).build(root(savedInstanceState))
}
