package com.badoo.ribs.samples.communication.menu_example.rib.menu_example

import com.badoo.ribs.core.Node
import com.badoo.ribs.core.modality.BuildParams
import com.badoo.ribs.core.plugin.Plugin
import com.badoo.ribs.core.view.ViewFactory

class MenuExampleNode internal constructor(
    buildParams: BuildParams<*>,
    viewFactory: ViewFactory<MenuExampleView>?,
    plugins: List<Plugin> = emptyList()
) : Node<MenuExampleView>(
    buildParams = buildParams,
    viewFactory = viewFactory,
    plugins = plugins
), MenuExample
