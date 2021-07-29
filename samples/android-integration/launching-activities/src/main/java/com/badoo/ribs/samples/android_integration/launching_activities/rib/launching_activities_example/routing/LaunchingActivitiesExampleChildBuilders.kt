package com.badoo.ribs.samples.android_integration.launching_activities.rib.launching_activities_example.routing

import com.badoo.ribs.samples.android_integration.launching_activities.rib.child1.LaunchingActivitiesChild1Builder
import com.badoo.ribs.samples.android_integration.launching_activities.rib.child2.LaunchingActivitiesChild2Builder
import com.badoo.ribs.samples.android_integration.launching_activities.rib.child_common.LaunchingActivitiesChildBase

class LaunchingActivitiesExampleChildBuilders(
    dependency: LaunchingActivitiesChildBase.Dependency
) {
    private val childDeps = SubtreeDependency(dependency)
    val child1: LaunchingActivitiesChild1Builder = LaunchingActivitiesChild1Builder(childDeps)
    val child2: LaunchingActivitiesChild2Builder = LaunchingActivitiesChild2Builder(childDeps)

    class SubtreeDependency(
        private val dependency: LaunchingActivitiesChildBase.Dependency
    ) : LaunchingActivitiesChildBase.Dependency by dependency
}
