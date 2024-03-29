package com.badoo.ribs.samples.android.launching_activities.rib.child_common

import com.badoo.ribs.android.activitystarter.CanProvideActivityStarter
import com.badoo.ribs.core.Rib

interface LaunchingActivitiesChildBase : Rib {
    interface Dependency : CanProvideActivityStarter
}
