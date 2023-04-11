package com.badoo.ribs.test

import com.badoo.ribs.core.customisation.RibCustomisation
import com.badoo.ribs.core.customisation.RibCustomisationDirectory
import com.badoo.ribs.core.customisation.RibCustomisationDirectoryImpl
import com.bumble.appyx.utils.customisations.put

fun RibCustomisation.toDirectory(): RibCustomisationDirectory =
    RibCustomisationDirectoryImpl().also { it.put { this } }
