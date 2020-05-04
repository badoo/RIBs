package com.badoo.ribs.core.helper

import android.os.Bundle
import com.badoo.ribs.core.AttachMode
import com.badoo.ribs.core.builder.BuildParams
import com.badoo.ribs.core.builder.BuildContext
import com.badoo.ribs.core.Rib
import com.badoo.ribs.core.routing.portal.AncestryInfo
import java.util.UUID

fun testBuildParams(
    savedInstanceState: Bundle? = null,
    ancestryInfo: AncestryInfo? = null
) = BuildParams<Nothing?>(
    payload = null,
    buildContext = if (ancestryInfo == null) {
        BuildContext.root(savedInstanceState)
    } else {
        BuildContext(
            ancestryInfo = ancestryInfo,
            attachMode = AttachMode.PARENT,
            savedInstanceState = savedInstanceState
        )
    },
    identifier = Rib.Identifier(
        uuid = UUID.randomUUID()
    )
)
