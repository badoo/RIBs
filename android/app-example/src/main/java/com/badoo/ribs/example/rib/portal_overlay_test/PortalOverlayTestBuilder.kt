package com.badoo.ribs.example.rib.portal_overlay_test

import com.badoo.ribs.core.builder.BuildParams
import com.badoo.ribs.core.builder.SimpleBuilder
import com.badoo.ribs.customisation.customisationsBranchFor
import com.badoo.ribs.customisation.getOrDefault

class PortalOverlayTestBuilder(
    dependency: PortalOverlayTest.Dependency
) : SimpleBuilder<PortalOverlayTestNode>(object : PortalOverlayTest {}) {

    private val dependency : PortalOverlayTest.Dependency = object : PortalOverlayTest.Dependency by dependency {
        override fun ribCustomisation() = dependency.customisationsBranchFor(PortalOverlayTest::class)
    }

    override fun build(buildParams: BuildParams<Nothing?>): PortalOverlayTestNode {
        val customisation = dependency.getOrDefault(PortalOverlayTest.Customisation())
        val interactor = PortalOverlayTestInteractor(buildParams)

        return PortalOverlayTestNode(
            buildParams = buildParams,
            viewFactory = customisation.viewFactory(null),
            interactor = interactor
        )
    }
}
