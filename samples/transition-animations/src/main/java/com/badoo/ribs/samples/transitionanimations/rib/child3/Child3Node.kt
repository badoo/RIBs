package com.badoo.ribs.samples.transitionanimations.rib.child3

import com.badoo.ribs.core.Node
import com.badoo.ribs.core.modality.BuildParams
import com.badoo.ribs.core.plugin.Plugin
import com.badoo.ribs.core.view.RibView

class Child3Node internal constructor(
    buildParams: BuildParams<*>,
    viewFactory: ((RibView) -> Child3View?)?,
    plugins: List<Plugin> = emptyList()
) : Node<Child3View>(
    buildParams = buildParams,
    viewFactory = viewFactory,
    plugins = plugins
), Child3
