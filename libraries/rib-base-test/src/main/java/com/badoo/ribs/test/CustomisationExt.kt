package com.badoo.ribs.test

import com.bumble.appyx.utils.customisations.NodeCustomisation
import com.bumble.appyx.utils.customisations.NodeCustomisationDirectoryImpl

fun NodeCustomisation.toDirectory(): NodeCustomisationDirectoryImpl =
    NodeCustomisationDirectoryImpl().also { it.put(this) }
