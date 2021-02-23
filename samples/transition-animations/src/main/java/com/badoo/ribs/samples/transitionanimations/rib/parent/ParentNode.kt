package com.badoo.ribs.samples.transitionanimations.rib.parent

import com.badoo.ribs.core.Node
import com.badoo.ribs.core.modality.BuildParams
import com.badoo.ribs.core.plugin.Plugin
import com.badoo.ribs.core.view.RibView

class ParentNode internal constructor(
    buildParams: BuildParams<*>,
    viewFactory: ((RibView) -> ParentView?)?,
    plugins: List<Plugin> = emptyList()
) : Node<ParentView>(
    buildParams = buildParams,
    viewFactory = viewFactory,
    plugins = plugins
), Parent
