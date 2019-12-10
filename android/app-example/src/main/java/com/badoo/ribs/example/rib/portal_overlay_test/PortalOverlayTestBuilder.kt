package com.badoo.ribs.example.rib.portal_overlay_test

import android.os.Bundle
import com.badoo.ribs.core.Builder
import com.badoo.ribs.customisation.customisationsBranchFor
import com.badoo.ribs.customisation.getOrDefault

class PortalOverlayTestBuilder(
    dependency: PortalOverlayTest.Dependency
) : Builder<PortalOverlayTest.Dependency>() {

    override val dependency : PortalOverlayTest.Dependency = object : PortalOverlayTest.Dependency by dependency {
        override fun ribCustomisation() = dependency.customisationsBranchFor(PortalOverlayTest::class)
    }

    fun build(savedInstanceState: Bundle?): PortalOverlayTestNode {
        val customisation = dependency.getOrDefault(PortalOverlayTest.Customisation())
        val router = PortalOverlayTestRouter(savedInstanceState)
        val interactor = PortalOverlayTestInteractor(savedInstanceState, router)

        return PortalOverlayTestNode(
            savedInstanceState,
            viewFactory = customisation.viewFactory(null),
            router = router,
            interactor = interactor
        )
    }
}
