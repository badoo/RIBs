package com.badoo.ribs.samples.retained_instance_store.rib

import com.badoo.ribs.core.Rib
import com.badoo.ribs.samples.retained_instance_store.utils.Clock
import com.badoo.ribs.samples.retained_instance_store.utils.ScreenOrientationController

interface RetainedInstanceRib : Rib {

    interface Dependency {
        val orientationController: ScreenOrientationController
        val clock: Clock
    }
}
