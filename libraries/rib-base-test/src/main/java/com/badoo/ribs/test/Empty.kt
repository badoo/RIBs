package com.badoo.ribs.test

import com.badoo.ribs.core.customisation.RibCustomisationDirectoryImpl
import com.badoo.ribs.core.modality.BuildParams

fun emptyBuildParams(): BuildParams<Nothing?> =
    BuildParams(
        payload = null,
        buildContext = com.badoo.ribs.core.modality.BuildContext.root(
            savedInstanceState = null,
            customisations = RibCustomisationDirectoryImpl()
        )
    )
