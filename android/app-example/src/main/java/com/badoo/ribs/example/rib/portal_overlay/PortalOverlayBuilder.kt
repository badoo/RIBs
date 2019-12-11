package com.badoo.ribs.example.rib.portal_overlay

import android.os.Bundle
import com.badoo.ribs.core.Builder
import com.badoo.ribs.customisation.customisationsBranchFor
import com.badoo.ribs.customisation.getOrDefault

class PortalOverlayBuilder(
    dependency: PortalOverlay.Dependency
) : Builder<PortalOverlay.Dependency>() {

    override val dependency : PortalOverlay.Dependency = object : PortalOverlay.Dependency by dependency {
        override fun ribCustomisation() = dependency.customisationsBranchFor(PortalOverlay::class)
    }

    fun build(savedInstanceState: Bundle?): PortalOverlayNode {
        val customisation = dependency.getOrDefault(PortalOverlay.Customisation())
        val router = PortalOverlayRouter(savedInstanceState)
        val interactor = PortalOverlayInteractor(savedInstanceState, router)

        return PortalOverlayNode(
            savedInstanceState,
            viewFactory = customisation.viewFactory(null),
            router = router,
            interactor = interactor
        )
    }
}
