package com.badoo.ribs.sandbox.rib.small

import com.badoo.ribs.core.Node
import com.badoo.ribs.core.modality.BuildParams
import com.badoo.ribs.core.plugin.Plugin
import com.badoo.ribs.core.view.ViewFactory

class SmallNode(
    buildParams: BuildParams<Nothing?>,
    viewFactory: ViewFactory<SmallView>?,
    plugins: List<Plugin>
) : Node<SmallView>(
    buildParams = buildParams,
    viewFactory = viewFactory,
    plugins = plugins
), Small {

}
