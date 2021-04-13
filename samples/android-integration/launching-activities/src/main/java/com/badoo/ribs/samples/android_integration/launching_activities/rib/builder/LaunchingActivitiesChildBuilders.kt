package com.badoo.ribs.samples.android_integration.launching_activities.rib.builder

import com.badoo.ribs.samples.android_integration.launching_activities.rib.LaunchingActivities

class LaunchingActivitiesChildBuilders(
        dependency: LaunchingActivities.Dependency
) {
    private val childDeps = SubtreeDependency(dependency)
    val child: LaunchingActivitiesChildBuilder = LaunchingActivitiesChildBuilder(childDeps)

    class SubtreeDependency(
            private val dependency: LaunchingActivities.Dependency
    ) : LaunchingActivities.Dependency by dependency
}
