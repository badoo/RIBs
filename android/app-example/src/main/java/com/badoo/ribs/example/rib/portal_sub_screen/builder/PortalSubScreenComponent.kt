package com.badoo.ribs.example.rib.portal_sub_screen.builder

import android.os.Bundle
import com.badoo.ribs.example.rib.portal_full_screen.PortalFullScreen
import com.badoo.ribs.example.rib.portal_overlay.PortalOverlay
import com.badoo.ribs.example.rib.portal_sub_screen.PortalSubScreen
import com.badoo.ribs.example.rib.portal_sub_screen.PortalSubScreenNode
import dagger.BindsInstance

@PortalSubScreenScope
@dagger.Component(
    modules = [PortalSubScreenModule::class],
    dependencies = [PortalSubScreen.Dependency::class]
)
internal interface PortalSubScreenComponent :
    PortalFullScreen.Dependency,
    PortalOverlay.Dependency {

    @dagger.Component.Factory
    interface Factory {
        fun create(
            dependency: PortalSubScreen.Dependency,
            @BindsInstance customisation: PortalSubScreen.Customisation,
            @BindsInstance savedInstanceState: Bundle?
        ): PortalSubScreenComponent
    }

    fun node(): PortalSubScreenNode
}
