package com.badoo.ribs.samples.transitionanimations.rib.child2

import com.badoo.ribs.core.Node
import com.badoo.ribs.core.modality.BuildParams
import com.badoo.ribs.core.plugin.Plugin
import com.badoo.ribs.core.view.RibView

class Child2Node internal constructor(
    buildParams: BuildParams<*>,
    viewFactory: ((RibView) -> Child2View?)?,
    plugins: List<Plugin> = emptyList()
) : Node<Child2View>(
    buildParams = buildParams,
    viewFactory = viewFactory,
    plugins = plugins
), Child2
