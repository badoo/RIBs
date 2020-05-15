package com.badoo.ribs.sandbox.rib.small

import android.view.ViewGroup
import com.badoo.ribs.core.Node
import com.badoo.ribs.core.builder.BuildParams
import com.badoo.ribs.core.plugin.Plugin

class SmallNode(
    buildParams: BuildParams<Nothing?>,
    viewFactory: ((ViewGroup) -> SmallView?)?,
    plugins: List<Plugin>
) : Node<SmallView>(
    buildParams = buildParams,
    viewFactory = viewFactory,
    plugins = plugins
), Small {

}
