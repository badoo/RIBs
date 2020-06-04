package com.badoo.ribs.sandbox.rib.small.builder

import com.badoo.ribs.core.modality.BuildParams
import com.badoo.ribs.sandbox.rib.big.Big
import com.badoo.ribs.sandbox.rib.portal_overlay_test.PortalOverlayTest
import com.badoo.ribs.sandbox.rib.small.Small
import com.badoo.ribs.sandbox.rib.small.SmallNode
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
            @BindsInstance buildParams: BuildParams<Nothing?>,
            @BindsInstance customisation: Small.Customisation
        ): SmallComponent
    }

    fun node(): SmallNode
}
