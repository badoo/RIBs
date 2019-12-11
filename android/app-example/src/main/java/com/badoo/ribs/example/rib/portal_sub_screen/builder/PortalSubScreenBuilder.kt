package com.badoo.ribs.example.rib.portal_sub_screen.builder

import android.os.Bundle
import com.badoo.ribs.core.Builder
import com.badoo.ribs.customisation.customisationsBranchFor
import com.badoo.ribs.customisation.getOrDefault
import com.badoo.ribs.example.rib.portal_sub_screen.PortalSubScreen
import com.badoo.ribs.example.rib.portal_sub_screen.PortalSubScreenNode

class PortalSubScreenBuilder(
    dependency: PortalSubScreen.Dependency
) : Builder<PortalSubScreen.Dependency>() {

    override val dependency : PortalSubScreen.Dependency = object : PortalSubScreen.Dependency by dependency {
        override fun ribCustomisation() = dependency.customisationsBranchFor(PortalSubScreen::class)
    }

    fun build(savedInstanceState: Bundle?): PortalSubScreenNode =
        DaggerPortalSubScreenComponent
            .factory()
            .create(
                dependency = dependency,
                customisation = dependency.getOrDefault(PortalSubScreen.Customisation()),
                savedInstanceState = savedInstanceState
            )
            .node()
}
