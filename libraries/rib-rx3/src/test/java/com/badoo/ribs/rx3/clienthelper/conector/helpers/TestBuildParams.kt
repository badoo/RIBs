package com.badoo.ribs.rx3.clienthelper.conector.helpers

import android.os.Bundle
import com.badoo.ribs.core.Rib
import com.badoo.ribs.core.customisation.RibCustomisationDirectoryImpl
import com.badoo.ribs.core.modality.ActivationMode
import com.badoo.ribs.core.modality.AncestryInfo
import com.badoo.ribs.core.modality.BuildContext
import com.badoo.ribs.core.modality.BuildParams
import java.util.UUID

fun testBuildParams(
    savedInstanceState: Bundle? = null,
    ancestryInfo: AncestryInfo? = null,
    identifier: Rib.Identifier = Rib.Identifier(
        uuid = UUID.randomUUID()
    )
) = BuildParams(
    payload = null,
    buildContext = if (ancestryInfo == null) {
        BuildContext.root(savedInstanceState)
    } else {
        BuildContext(
            ancestryInfo = ancestryInfo,
            activationMode = ActivationMode.ATTACH_TO_PARENT,
            savedInstanceState = savedInstanceState,
            customisations = RibCustomisationDirectoryImpl()
        )
    },
    identifier = identifier
)
