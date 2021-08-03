package com.badoo.ribs.samples.routing.parameterised_routing.rib.profile

import com.badoo.ribs.core.Rib

interface Profile : Rib {

    data class Params(
        val profileId: Int
    )
}
