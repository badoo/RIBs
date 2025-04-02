package com.badoo.ribs.sandbox.rib.small.routing

import com.badoo.ribs.sandbox.rib.big.Big
import com.badoo.ribs.sandbox.rib.big.BigBuilder
import com.badoo.ribs.sandbox.rib.portal_overlay_test.PortalOverlayTest
import com.badoo.ribs.sandbox.rib.portal_overlay_test.PortalOverlayTestBuilder
import com.badoo.ribs.sandbox.rib.small.Small

internal open class SmallChildBuilders(
    dependency: Small.Dependency
) {
    private val subtreeDeps = SubtreeDependency(dependency)

    val portalOverlay = PortalOverlayTestBuilder(subtreeDeps)
    val big = BigBuilder(subtreeDeps)

    class SubtreeDependency(
        dependency: Small.Dependency
    ) : Small.Dependency by dependency,
        PortalOverlayTest.Dependency,
        Big.Dependency
}
