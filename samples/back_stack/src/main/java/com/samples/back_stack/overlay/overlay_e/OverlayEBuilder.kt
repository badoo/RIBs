package com.samples.back_stack.overlay.overlay_e

import com.badoo.ribs.builder.SimpleBuilder
import com.badoo.ribs.core.modality.BuildParams

class OverlayEBuilder(
    private val dependency: OverlayE.Dependency
) : SimpleBuilder<OverlayE>() {

    override fun build(buildParams: BuildParams<Nothing?>): OverlayE {
        return OverlayENode(
            buildParams = buildParams,
            viewFactory = OverlayEViewImpl.Factory().invoke(null),
            plugins = emptyList()
        )
    }

}
