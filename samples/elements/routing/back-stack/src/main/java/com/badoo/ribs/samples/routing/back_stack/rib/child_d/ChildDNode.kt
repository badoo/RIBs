package com.badoo.ribs.samples.routing.back_stack.rib.child_d

import com.badoo.ribs.core.Node
import com.badoo.ribs.core.modality.BuildParams
import com.badoo.ribs.core.plugin.Plugin
import com.badoo.ribs.core.view.ViewFactory

class ChildDNode internal constructor(
    buildParams: BuildParams<*>,
    viewFactory: ViewFactory<ChildDView>?,
    plugins: List<Plugin> = emptyList()
) : Node<ChildDView>(
    buildParams = buildParams,
    viewFactory = viewFactory,
    plugins = plugins
), ChildD
