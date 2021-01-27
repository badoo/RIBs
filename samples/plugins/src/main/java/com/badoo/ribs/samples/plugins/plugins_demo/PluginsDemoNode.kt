package com.badoo.ribs.samples.plugins.plugins_demo

import com.badoo.ribs.core.Node
import com.badoo.ribs.core.modality.BuildParams
import com.badoo.ribs.core.plugin.Plugin
import com.badoo.ribs.core.view.RibView

class PluginsDemoNode internal constructor(
    buildParams: BuildParams<*>,
    viewFactory: ((RibView) -> PluginsDemoView?)?,
    plugins: List<Plugin> = emptyList()
) : Node<PluginsDemoView>(
    buildParams = buildParams,
    viewFactory = viewFactory,
    plugins = plugins
), PluginsDemo
