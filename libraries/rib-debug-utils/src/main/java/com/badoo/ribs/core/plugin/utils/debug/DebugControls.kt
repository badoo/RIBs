package com.badoo.ribs.core.plugin.utils.debug

import android.view.View
import android.view.ViewGroup
import com.badoo.ribs.core.Rib

abstract class DebugControls<T : Rib>(
    override val label: String,
    viewFactory: ((ViewGroup) -> View)
) : AbstractDebugControls<T>(
    viewFactory = viewFactory
)
