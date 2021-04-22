package com.badoo.ribs.samples.android_integration.launching_activities.rib.launching_activities_child_common

import com.badoo.ribs.android.activitystarter.CanProvideActivityStarter
import com.badoo.ribs.core.Rib

interface LaunchingActivitiesChildBase : Rib {
    interface Dependency : CanProvideActivityStarter
}
