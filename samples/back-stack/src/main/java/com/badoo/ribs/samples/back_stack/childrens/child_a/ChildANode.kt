package com.badoo.ribs.samples.back_stack.childrens.child_a

import com.badoo.ribs.core.Node
import com.badoo.ribs.core.modality.BuildParams
import com.badoo.ribs.core.plugin.Plugin
import com.badoo.ribs.core.view.RibView

class ChildANode internal constructor(
    buildParams: BuildParams<*>,
    viewFactory: ((RibView) -> ChildAView?)?,
    plugins: List<Plugin> = emptyList()
) : Node<ChildAView>(
    buildParams = buildParams,
    viewFactory = viewFactory,
    plugins = plugins
), ChildA
