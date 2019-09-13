package com.badoo.ribs.example.rib.portal_overlay_test.builder

import android.os.Bundle
import com.badoo.ribs.core.Builder
import com.badoo.ribs.customisation.customisationsBranchFor
import com.badoo.ribs.customisation.getOrDefault
import com.badoo.ribs.example.rib.portal_overlay_test.PortalOverlayTest
import com.badoo.ribs.example.rib.portal_overlay_test.PortalOverlayTestNode

class PortalOverlayTestBuilder(
    dependency: PortalOverlayTest.Dependency
) : Builder<PortalOverlayTest.Dependency>() {

    override val dependency : PortalOverlayTest.Dependency = object : PortalOverlayTest.Dependency by dependency {
        override fun ribCustomisation() = dependency.customisationsBranchFor(PortalOverlayTest::class)
    }

    fun build(savedInstanceState: Bundle?): PortalOverlayTestNode =
        DaggerPortalOverlayTestComponent
            .factory()
            .create(
                dependency = dependency,
                customisation = dependency.getOrDefault(PortalOverlayTest.Customisation()),
                savedInstanceState = savedInstanceState
            )
            .node()
}
