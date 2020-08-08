package com.badoo.ribs.portal

import com.badoo.ribs.core.Node
import com.badoo.ribs.core.modality.BuildParams
import com.badoo.ribs.core.plugin.Plugin

internal class PortalNode(
    buildParams: BuildParams<*>,
    plugins: List<Plugin>
) : Node<Nothing>(
    buildParams = buildParams,
    viewFactory = null,
    plugins = plugins
), Portal {

}
