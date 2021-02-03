package com.badoo.ribs.samples.buildtime.rib.profile

import com.badoo.ribs.core.Rib

interface Profile : Rib {

    data class Params(
        val profileId: Int
    )
}
