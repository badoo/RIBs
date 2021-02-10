package com.badoo.ribs.samples.retained_instance_store.rib.counter

import com.badoo.ribs.core.Rib
import com.badoo.ribs.samples.retained_instance_store.utils.Clock

interface Counter : Rib {

    interface Dependency {
        val clock: Clock
        val isRetained: Boolean
    }
}
