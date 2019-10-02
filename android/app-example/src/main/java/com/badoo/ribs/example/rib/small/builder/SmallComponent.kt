package com.badoo.ribs.example.rib.small.builder

import com.badoo.ribs.core.BuildContext
import com.badoo.ribs.example.rib.big.Big
import com.badoo.ribs.example.rib.portal_overlay_test.PortalOverlayTest
import com.badoo.ribs.example.rib.small.Small
import com.badoo.ribs.example.rib.small.SmallNode
import dagger.BindsInstance

@SmallScope
@dagger.Component(
    modules = [SmallModule::class],
    dependencies = [Small.Dependency::class]
)
internal interface SmallComponent :
    Big.Dependency,
    PortalOverlayTest.Dependency {

    @dagger.Component.Factory
    interface Factory {
        fun create(
            dependency: Small.Dependency,
            @BindsInstance buildContext: BuildContext.Resolved<Nothing?>,
            @BindsInstance customisation: Small.Customisation
        ): SmallComponent
    }

    fun node(): SmallNode
}
