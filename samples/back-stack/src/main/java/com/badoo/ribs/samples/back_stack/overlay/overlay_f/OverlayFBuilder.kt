package com.badoo.ribs.samples.back_stack.overlay.overlay_f

import com.badoo.ribs.builder.SimpleBuilder
import com.badoo.ribs.core.modality.BuildParams

class OverlayFBuilder(
    private val dependency: OverlayF.Dependency
) : SimpleBuilder<OverlayF>() {

    override fun build(buildParams: BuildParams<Nothing?>): OverlayF {
        return OverlayFNode(
            buildParams = buildParams,
            viewFactory = OverlayFViewImpl.Factory().invoke(null),
            plugins = emptyList()
        )
    }

}
