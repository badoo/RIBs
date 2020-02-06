package com.badoo.ribs.core.helper

import android.os.Bundle
import com.badoo.ribs.core.AttachMode
import com.badoo.ribs.core.builder.BuildParams
import com.badoo.ribs.core.builder.BuildContext
import com.badoo.ribs.core.Rib
import com.badoo.ribs.core.routing.portal.AncestryInfo
import java.util.UUID

fun testBuildParams(
    rib: Rib = object : TestPublicRibInterface {},
    savedInstanceState: Bundle? = null
) = BuildParams<Nothing?>(
    data = null,
    buildContext = BuildContext(
        ancestryInfo = AncestryInfo.Root,
        viewAttachMode = AttachMode.PARENT,
        savedInstanceState = savedInstanceState
    ),
    identifier = Rib.Identifier(
        rib = rib,
        uuid = UUID.randomUUID()
    )
)
