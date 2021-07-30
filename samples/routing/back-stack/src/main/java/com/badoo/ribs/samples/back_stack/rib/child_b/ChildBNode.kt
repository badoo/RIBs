package com.badoo.ribs.samples.back_stack.rib.child_b

import com.badoo.ribs.core.Node
import com.badoo.ribs.core.modality.BuildParams
import com.badoo.ribs.core.plugin.Plugin
import com.badoo.ribs.core.view.ViewFactory

class ChildBNode internal constructor(
    buildParams: BuildParams<*>,
    viewFactory: ViewFactory<ChildBView>?,
    plugins: List<Plugin> = emptyList()
) : Node<ChildBView>(
    buildParams = buildParams,
    viewFactory = viewFactory,
    plugins = plugins
), ChildB
