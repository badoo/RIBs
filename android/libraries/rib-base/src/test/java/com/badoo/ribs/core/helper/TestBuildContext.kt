package com.badoo.ribs.core.helper

import android.os.Bundle
import com.badoo.ribs.core.BuildContext
import com.badoo.ribs.core.Rib
import com.badoo.ribs.core.routing.portal.AncestryInfo
import java.util.UUID

fun testBuildContext(
    rib: Rib = object : TestPublicRibInterface {},
    savedInstanceState: Bundle? = null
) = BuildContext.Resolved<Nothing?>(
    ancestryInfo = AncestryInfo.Root,
    savedInstanceState = savedInstanceState,
    identifier = Rib.Identifier(
        rib = rib,
        uuid = UUID.randomUUID(),
        tag = ""
    ),
    data = null
)
