package com.badoo.ribs.samples.back_stack.parent.routing

import com.badoo.ribs.samples.back_stack.content.content_a.ContentA
import com.badoo.ribs.samples.back_stack.content.content_a.ContentABuilder
import com.badoo.ribs.samples.back_stack.content.content_b.ContentB
import com.badoo.ribs.samples.back_stack.content.content_b.ContentBBuilder
import com.badoo.ribs.samples.back_stack.content.content_c.ContentC
import com.badoo.ribs.samples.back_stack.content.content_c.ContentCBuilder
import com.badoo.ribs.samples.back_stack.content.content_d.ContentD
import com.badoo.ribs.samples.back_stack.content.content_d.ContentDBuilder
import com.badoo.ribs.samples.back_stack.overlay.overlay_e.OverlayE
import com.badoo.ribs.samples.back_stack.overlay.overlay_e.OverlayEBuilder
import com.badoo.ribs.samples.back_stack.overlay.overlay_f.OverlayF
import com.badoo.ribs.samples.back_stack.overlay.overlay_f.OverlayFBuilder
import com.badoo.ribs.samples.back_stack.parent.Parent

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

