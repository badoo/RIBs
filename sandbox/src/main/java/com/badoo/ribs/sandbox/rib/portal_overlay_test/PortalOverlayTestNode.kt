package com.badoo.ribs.sandbox.rib.portal_overlay_test

import android.view.ViewGroup
import com.badoo.ribs.core.Node
import com.badoo.ribs.core.builder.BuildParams
import com.badoo.ribs.core.plugin.Plugin

class PortalOverlayTestNode(
    buildParams: BuildParams<*>,
    viewFactory: ((ViewGroup) -> PortalOverlayTestView?)?,
    plugins: List<Plugin>
) : Node<PortalOverlayTestView>(
    buildParams = buildParams,
    viewFactory = viewFactory,
    plugins = plugins
), PortalOverlayTest {

}
