package com.badoo.ribs.samples.android.launching_activities.rib.launching_activities_example

import com.badoo.ribs.android.activitystarter.CanProvideActivityStarter
import com.badoo.ribs.core.Rib

interface LaunchingActivitiesExample : Rib {
    interface Dependency : CanProvideActivityStarter
}
