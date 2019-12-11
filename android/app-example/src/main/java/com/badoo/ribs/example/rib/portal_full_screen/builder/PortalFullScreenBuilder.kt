package com.badoo.ribs.example.rib.portal_full_screen.builder

import android.os.Bundle
import com.badoo.ribs.core.Builder
import com.badoo.ribs.customisation.customisationsBranchFor
import com.badoo.ribs.customisation.getOrDefault
import com.badoo.ribs.example.rib.portal_full_screen.PortalFullScreen
import com.badoo.ribs.example.rib.portal_full_screen.PortalFullScreenNode

class PortalFullScreenBuilder(
    dependency: PortalFullScreen.Dependency
) : Builder<PortalFullScreen.Dependency>() {

    override val dependency : PortalFullScreen.Dependency = object : PortalFullScreen.Dependency by dependency {
        override fun ribCustomisation() = dependency.customisationsBranchFor(PortalFullScreen::class)
    }

    fun build(savedInstanceState: Bundle?): PortalFullScreenNode =
        DaggerPortalFullScreenComponent
            .factory()
            .create(
                dependency = dependency,
                customisation = dependency.getOrDefault(PortalFullScreen.Customisation()),
                savedInstanceState = savedInstanceState
            )
            .node()
}
