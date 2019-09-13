package com.badoo.ribs.example.rib.portal_overlay_test.builder

import android.os.Bundle
import com.badoo.ribs.example.rib.portal_overlay_test.PortalOverlayTest
import com.badoo.ribs.example.rib.portal_overlay_test.PortalOverlayTestNode
import dagger.BindsInstance

@PortalOverlayTestScope
@dagger.Component(
    modules = [PortalOverlayTestModule::class],
    dependencies = [PortalOverlayTest.Dependency::class]
)
internal interface PortalOverlayTestComponent {

    @dagger.Component.Factory
    interface Factory {
        fun create(
            dependency: PortalOverlayTest.Dependency,
            @BindsInstance customisation: PortalOverlayTest.Customisation,
            @BindsInstance savedInstanceState: Bundle?
        ): PortalOverlayTestComponent
    }

    fun node(): PortalOverlayTestNode
}
