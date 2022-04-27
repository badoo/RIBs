package com.badoo.ribs.test

import com.badoo.ribs.core.modality.BuildParams
import com.bumble.appyx.utils.customisations.NodeCustomisationDirectoryImpl

fun emptyBuildParams(): BuildParams<Nothing?> =
    BuildParams(
        payload = null,
        buildContext = com.badoo.ribs.core.modality.BuildContext.root(
            savedInstanceState = null,
            customisations = NodeCustomisationDirectoryImpl()
        )
    )
