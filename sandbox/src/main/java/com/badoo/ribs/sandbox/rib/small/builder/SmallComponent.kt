package com.badoo.ribs.sandbox.rib.small.builder

import com.badoo.ribs.sandbox.rib.big.Big
import com.badoo.ribs.sandbox.rib.portal_overlay_test.Dependency
import com.badoo.ribs.sandbox.rib.portal_overlay_test.PortalOverlayTest
import com.badoo.ribs.sandbox.rib.small.Small
import com.badoo.ribs.sandbox.rib.small.SmallNode

@SmallScope
//@dagger.Component(
//    modules = [SmallModule::class],
//    dependencies = [Small.Dependency::class, Small.ExtraDependencies::class]
//)
internal interface SmallComponent {
//    Big.Dependency,
//    Dependency {

    fun node(): SmallNode
}
