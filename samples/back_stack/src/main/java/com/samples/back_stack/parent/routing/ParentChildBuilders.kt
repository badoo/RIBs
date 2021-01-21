package com.samples.back_stack.parent.routing

import com.samples.back_stack.content.content_a.ContentA
import com.samples.back_stack.content.content_a.ContentABuilder
import com.samples.back_stack.content.content_b.ContentB
import com.samples.back_stack.content.content_b.ContentBBuilder
import com.samples.back_stack.content.content_c.ContentC
import com.samples.back_stack.content.content_c.ContentCBuilder
import com.samples.back_stack.content.content_d.ContentD
import com.samples.back_stack.content.content_d.ContentDBuilder
import com.samples.back_stack.overlay.overlay_e.OverlayE
import com.samples.back_stack.overlay.overlay_e.OverlayEBuilder
import com.samples.back_stack.overlay.overlay_f.OverlayF
import com.samples.back_stack.overlay.overlay_f.OverlayFBuilder
import com.samples.back_stack.parent.Parent

internal open class ParentChildBuilders(
    dependency: Parent.Dependency
) {

    private val subtreeDeps = SubtreeDependency(dependency)

    val contentA = ContentABuilder(subtreeDeps)
    val contentB = ContentBBuilder(subtreeDeps)
    val contentC = ContentCBuilder(subtreeDeps)
    val contentD = ContentDBuilder(subtreeDeps)

    val overlayE = OverlayEBuilder(subtreeDeps)
    val overlayF = OverlayFBuilder(subtreeDeps)

    class SubtreeDependency(
        dependency: Parent.Dependency
    ) : Parent.Dependency by dependency,
        ContentA.Dependency,
        ContentD.Dependency,
        ContentB.Dependency,
        ContentC.Dependency,
        OverlayE.Dependency,
        OverlayF.Dependency

}

