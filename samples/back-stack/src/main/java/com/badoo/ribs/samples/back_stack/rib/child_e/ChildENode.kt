package com.badoo.ribs.samples.back_stack.rib.child_e

import com.badoo.ribs.core.Node
import com.badoo.ribs.core.modality.BuildParams
import com.badoo.ribs.core.plugin.Plugin
import com.badoo.ribs.core.view.RibView

class ChildENode internal constructor(
    buildParams: BuildParams<*>,
    viewFactory: ((RibView) -> ChildEView?)?,
    plugins: List<Plugin> = emptyList()
) : Node<ChildEView>(
    buildParams = buildParams,
    viewFactory = viewFactory,
    plugins = plugins
), ChildE
