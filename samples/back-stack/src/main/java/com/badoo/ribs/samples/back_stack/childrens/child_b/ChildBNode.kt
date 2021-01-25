package com.badoo.ribs.samples.back_stack.childrens.child_b

import com.badoo.ribs.core.Node
import com.badoo.ribs.core.modality.BuildParams
import com.badoo.ribs.core.plugin.Plugin
import com.badoo.ribs.core.view.RibView

class ChildBNode internal constructor(
    buildParams: BuildParams<*>,
    viewFactory: ((RibView) -> ChildBView?)?,
    plugins: List<Plugin> = emptyList()
) : Node<ChildBView>(
    buildParams = buildParams,
    viewFactory = viewFactory,
    plugins = plugins
), ChildB
