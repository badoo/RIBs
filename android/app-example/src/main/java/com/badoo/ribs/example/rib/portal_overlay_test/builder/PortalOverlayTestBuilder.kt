package com.badoo.ribs.example.rib.portal_overlay_test.builder

import com.badoo.ribs.core.builder.BuildParams
import com.badoo.ribs.core.builder.Builder
import com.badoo.ribs.core.Rib
import com.badoo.ribs.customisation.customisationsBranchFor
import com.badoo.ribs.customisation.getOrDefault
import com.badoo.ribs.example.rib.portal_overlay_test.PortalOverlayTest
import com.badoo.ribs.example.rib.portal_overlay_test.PortalOverlayTestNode

class PortalOverlayTestBuilder(
    dependency: PortalOverlayTest.Dependency
) : Builder<PortalOverlayTest.Dependency, PortalOverlayTestNode>() {

    override val dependency : PortalOverlayTest.Dependency = object : PortalOverlayTest.Dependency by dependency {
        override fun ribCustomisation() = dependency.customisationsBranchFor(PortalOverlayTest::class)
    }

    override val rib: Rib =
        object : PortalOverlayTest {}

    override fun build(buildParams: BuildParams<Nothing?>): PortalOverlayTestNode =
        DaggerPortalOverlayTestComponent
            .factory()
            .create(
                dependency = dependency,
                customisation = dependency.getOrDefault(PortalOverlayTest.Customisation()),
                buildParams = buildParams
            )
            .node()
}
