package com.samples.back_stack.overlay.overlay_f

import com.badoo.ribs.core.Node
import com.badoo.ribs.core.modality.BuildParams
import com.badoo.ribs.core.plugin.Plugin
import com.badoo.ribs.core.view.RibView

class OverlayFNode internal constructor(
    buildParams: BuildParams<*>,
    viewFactory: ((RibView) -> OverlayFView?)?,
    plugins: List<Plugin> = emptyList()
) : Node<OverlayFView>(
    buildParams = buildParams,
    viewFactory = viewFactory,
    plugins = plugins
), OverlayF
