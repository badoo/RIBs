package com.badoo.ribs.samples.back_stack.content.content_c

import com.badoo.ribs.core.Node
import com.badoo.ribs.core.modality.BuildParams
import com.badoo.ribs.core.plugin.Plugin
import com.badoo.ribs.core.view.RibView

class ContentCNode internal constructor(
    buildParams: BuildParams<*>,
    viewFactory: ((RibView) -> ContentCView?)?,
    plugins: List<Plugin> = emptyList()
) : Node<ContentCView>(
    buildParams = buildParams,
    viewFactory = viewFactory,
    plugins = plugins
), ContentC
