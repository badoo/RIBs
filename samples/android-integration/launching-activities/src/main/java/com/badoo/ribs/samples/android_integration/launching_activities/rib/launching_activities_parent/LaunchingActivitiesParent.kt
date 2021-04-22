package com.badoo.ribs.samples.android_integration.launching_activities.rib.launching_activities_parent

import com.badoo.ribs.android.activitystarter.CanProvideActivityStarter
import com.badoo.ribs.core.Rib

interface LaunchingActivitiesParent : Rib {
    interface Dependency : CanProvideActivityStarter
}
