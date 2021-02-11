package com.badoo.ribs.samples.retained_instance_store.rib.counter

import com.badoo.ribs.core.Rib

interface Counter : Rib {

    data class Params(
        val isRetained: Boolean
    )
}
