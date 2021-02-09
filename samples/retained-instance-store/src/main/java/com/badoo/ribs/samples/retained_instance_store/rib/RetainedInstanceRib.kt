package com.badoo.ribs.samples.retained_instance_store.rib

import android.app.Activity
import com.badoo.ribs.core.Rib

interface RetainedInstanceRib : Rib {

    interface Dependency {
        //TODO wrap this
        val orientationController: Activity
    }
}
