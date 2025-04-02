package com.badoo.ribs.sandbox.rib.portal_overlay_test

import com.badoo.ribs.core.Node
import com.badoo.ribs.core.modality.BuildParams
import com.badoo.ribs.core.plugin.Plugin
import com.badoo.ribs.core.view.ViewFactory

class PortalOverlayTestNode(
    buildParams: BuildParams<*>,
    viewFactory: ViewFactory<PortalOverlayTestView>?,
    plugins: List<Plugin>
) : Node<PortalOverlayTestView>(
    buildParams = buildParams,
    viewFactory = viewFactory,
    plugins = plugins
), PortalOverlayTest
