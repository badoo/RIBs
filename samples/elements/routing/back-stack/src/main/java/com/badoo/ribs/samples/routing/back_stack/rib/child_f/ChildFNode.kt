package com.badoo.ribs.samples.routing.back_stack.rib.child_f

import com.badoo.ribs.core.Node
import com.badoo.ribs.core.modality.BuildParams
import com.badoo.ribs.core.plugin.Plugin
import com.badoo.ribs.core.view.ViewFactory

class ChildFNode internal constructor(
    buildParams: BuildParams<*>,
    viewFactory: ViewFactory<ChildFView>?,
    plugins: List<Plugin> = emptyList()
) : Node<ChildFView>(
    buildParams = buildParams,
    viewFactory = viewFactory,
    plugins = plugins
), ChildF
