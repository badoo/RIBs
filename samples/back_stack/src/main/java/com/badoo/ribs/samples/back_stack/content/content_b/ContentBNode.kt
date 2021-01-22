package com.badoo.ribs.samples.back_stack.content.content_b

import com.badoo.ribs.core.Node
import com.badoo.ribs.core.modality.BuildParams
import com.badoo.ribs.core.plugin.Plugin
import com.badoo.ribs.core.view.RibView

class ContentBNode internal constructor(
    buildParams: BuildParams<*>,
    viewFactory: ((RibView) -> ContentBView?)?,
    plugins: List<Plugin> = emptyList()
) : Node<ContentBView>(
    buildParams = buildParams,
    viewFactory = viewFactory,
    plugins = plugins
), ContentB
