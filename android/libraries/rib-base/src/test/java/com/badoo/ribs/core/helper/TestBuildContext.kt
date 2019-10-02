package com.badoo.ribs.core.helper

import com.badoo.ribs.core.BuildContext
import com.badoo.ribs.core.Rib
import com.badoo.ribs.core.routing.portal.AncestryInfo
import java.util.UUID

fun testBuildContext(rib: Rib = object : TestPublicRibInterface {}) = BuildContext.Resolved<Nothing?>(
    ancestryInfo = AncestryInfo.Root,
    savedInstanceState = null,
    identifier = Rib.Identifier(
        rib = rib,
        uuid = UUID.randomUUID(),
        tag = ""
    ),
    data = null
)
