package com.badoo.ribs.samples.routing.transition_animations.rib.child1

import com.badoo.ribs.core.Node
import com.badoo.ribs.core.modality.BuildParams
import com.badoo.ribs.core.plugin.Plugin
import com.badoo.ribs.core.view.ViewFactory

class Child1Node internal constructor(
    buildParams: BuildParams<*>,
    viewFactory: ViewFactory<Child1View>?,
    plugins: List<Plugin> = emptyList()
) : Node<Child1View>(
    buildParams = buildParams,
    viewFactory = viewFactory,
    plugins = plugins
), Child1
