package com.badoo.ribs.builder

import com.badoo.ribs.core.Rib
import com.badoo.ribs.core.modality.BuildContext

/**
 * Simplified version of [Builder] that doesn't have build-time payload.
 */
abstract class SimpleBuilder<T : Rib> : Builder<Nothing?, T>() {

    fun build(buildContext: BuildContext): T =
        build(buildContext, null)
}


