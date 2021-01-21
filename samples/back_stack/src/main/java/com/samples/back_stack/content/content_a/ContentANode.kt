package com.samples.back_stack.content.content_a

import com.badoo.ribs.core.Node
import com.badoo.ribs.core.modality.BuildParams
import com.badoo.ribs.core.plugin.Plugin
import com.badoo.ribs.core.view.RibView

class ContentANode internal constructor(
    buildParams: BuildParams<*>,
    viewFactory: ((RibView) -> ContentAView?)?,
    plugins: List<Plugin> = emptyList()
) : Node<ContentAView>(
    buildParams = buildParams,
    viewFactory = viewFactory,
    plugins = plugins
), ContentA
