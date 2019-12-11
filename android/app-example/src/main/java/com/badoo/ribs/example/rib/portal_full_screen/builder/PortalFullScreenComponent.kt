package com.badoo.ribs.example.rib.portal_full_screen.builder

import android.os.Bundle
import com.badoo.ribs.example.rib.portal_full_screen.PortalFullScreen
import com.badoo.ribs.example.rib.portal_full_screen.PortalFullScreenNode
import com.badoo.ribs.example.rib.portal_sub_screen.PortalSubScreen
import dagger.BindsInstance

@PortalFullScreenScope
@dagger.Component(
    modules = [PortalFullScreenModule::class],
    dependencies = [PortalFullScreen.Dependency::class]
)
internal interface PortalFullScreenComponent : PortalSubScreen.Dependency {

    @dagger.Component.Factory
    interface Factory {
        fun create(
            dependency: PortalFullScreen.Dependency,
            @BindsInstance customisation: PortalFullScreen.Customisation,
            @BindsInstance savedInstanceState: Bundle?
        ): PortalFullScreenComponent
    }

    fun node(): PortalFullScreenNode
}
