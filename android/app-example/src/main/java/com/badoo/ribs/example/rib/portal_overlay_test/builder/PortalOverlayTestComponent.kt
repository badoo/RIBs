package com.badoo.ribs.example.rib.portal_overlay_test.builder

import com.badoo.ribs.core.builder.BuildParams
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
            @BindsInstance buildParams: BuildParams<Nothing?>
        ): PortalOverlayTestComponent
    }

    fun node(): PortalOverlayTestNode
}
