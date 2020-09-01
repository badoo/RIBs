package com.badoo.ribs.core.plugin.utils.debug

import android.view.ViewGroup
import com.badoo.ribs.core.Rib

class DebugControlsHost(
    viewGroupForChildren: (() -> ViewGroup),
    growthDirection: GrowthDirection = GrowthDirection.TOP
) : AbstractDebugControls<Rib>(
    viewGroupForChildren = viewGroupForChildren,
    growthDirection = growthDirection
) {

    override val label: String = ""
}
