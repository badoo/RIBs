package com.badoo.ribs.samples.menu_example.rib.child3

import com.badoo.ribs.core.Node
import com.badoo.ribs.core.modality.BuildParams
import com.badoo.ribs.core.plugin.Plugin
import com.badoo.ribs.core.view.ViewFactory

class Child3Node internal constructor(
    buildParams: BuildParams<*>,
    viewFactory: ViewFactory<Child3View>?,
    plugins: List<Plugin> = emptyList()
) : Node<Child3View>(
    buildParams = buildParams,
    viewFactory = viewFactory,
    plugins = plugins
), Child3
