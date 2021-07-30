package com.badoo.ribs.samples.back_stack.rib.child_c

import com.badoo.ribs.core.Node
import com.badoo.ribs.core.modality.BuildParams
import com.badoo.ribs.core.plugin.Plugin
import com.badoo.ribs.core.view.ViewFactory

class ChildCNode internal constructor(
    buildParams: BuildParams<*>,
    viewFactory: ViewFactory<ChildCView>?,
    plugins: List<Plugin> = emptyList()
) : Node<ChildCView>(
    buildParams = buildParams,
    viewFactory = viewFactory,
    plugins = plugins
), ChildC
