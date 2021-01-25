package com.badoo.ribs.samples.back_stack.childrens.child_f

import com.badoo.ribs.core.Node
import com.badoo.ribs.core.modality.BuildParams
import com.badoo.ribs.core.plugin.Plugin
import com.badoo.ribs.core.view.RibView

class ChildFNode internal constructor(
    buildParams: BuildParams<*>,
    viewFactory: ((RibView) -> ChildFView?)?,
    plugins: List<Plugin> = emptyList()
) : Node<ChildFView>(
    buildParams = buildParams,
    viewFactory = viewFactory,
    plugins = plugins
), ChildF
