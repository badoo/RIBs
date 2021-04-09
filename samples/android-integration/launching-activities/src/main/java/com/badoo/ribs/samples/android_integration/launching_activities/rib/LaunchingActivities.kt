package com.badoo.ribs.samples.android_integration.launching_activities.rib

import com.badoo.ribs.android.activitystarter.CanProvideActivityStarter
import com.badoo.ribs.core.Rib

interface LaunchingActivities : Rib {
    interface Dependency : CanProvideActivityStarter
}
