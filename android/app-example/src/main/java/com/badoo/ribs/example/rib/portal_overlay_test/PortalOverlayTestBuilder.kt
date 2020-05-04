package com.badoo.ribs.example.rib.portal_overlay_test

import com.badoo.ribs.core.builder.BuildParams
import com.badoo.ribs.core.builder.SimpleBuilder

class PortalOverlayTestBuilder(
    private val dependency: PortalOverlayTest.Dependency
) : SimpleBuilder<PortalOverlayTest>() {

    override fun build(buildParams: BuildParams<Nothing?>): PortalOverlayTest {
        val customisation = buildParams.getOrDefault(PortalOverlayTest.Customisation())
        val interactor = PortalOverlayTestInteractor(buildParams)

        return PortalOverlayTestNode(
            buildParams = buildParams,
            viewFactory = customisation.viewFactory(null),
            interactor = interactor
        )
    }
}
