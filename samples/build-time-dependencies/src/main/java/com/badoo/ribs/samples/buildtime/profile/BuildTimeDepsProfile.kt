package com.badoo.ribs.samples.buildtime.profile

import com.badoo.ribs.core.Rib

interface BuildTimeDepsProfile : Rib {

    data class Params(
        val profileId: Int
    )
}
