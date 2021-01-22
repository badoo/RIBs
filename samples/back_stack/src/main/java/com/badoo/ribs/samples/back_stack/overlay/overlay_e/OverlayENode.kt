package com.badoo.ribs.samples.back_stack.overlay.overlay_e

import com.badoo.ribs.core.Node
import com.badoo.ribs.core.modality.BuildParams
import com.badoo.ribs.core.plugin.Plugin
import com.badoo.ribs.core.view.RibView

class OverlayENode internal constructor(
    buildParams: BuildParams<*>,
    viewFactory: ((RibView) -> OverlayEView?)?,
    plugins: List<Plugin> = emptyList()
) : Node<OverlayEView>(
    buildParams = buildParams,
    viewFactory = viewFactory,
    plugins = plugins
), OverlayE
