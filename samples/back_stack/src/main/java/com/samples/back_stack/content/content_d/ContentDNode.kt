package com.samples.back_stack.content.content_d

import com.badoo.ribs.core.Node
import com.badoo.ribs.core.modality.BuildParams
import com.badoo.ribs.core.plugin.Plugin
import com.badoo.ribs.core.view.RibView

class ContentDNode internal constructor(
    buildParams: BuildParams<*>,
    viewFactory: ((RibView) -> ContentDView?)?,
    plugins: List<Plugin> = emptyList()
) : Node<ContentDView>(
    buildParams = buildParams,
    viewFactory = viewFactory,
    plugins = plugins
), ContentD
