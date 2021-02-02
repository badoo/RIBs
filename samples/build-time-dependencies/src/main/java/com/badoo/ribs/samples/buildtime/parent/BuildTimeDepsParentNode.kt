package com.badoo.ribs.samples.buildtime.parent

import com.badoo.ribs.core.Node
import com.badoo.ribs.core.modality.BuildParams
import com.badoo.ribs.core.plugin.Plugin
import com.badoo.ribs.core.view.RibView

class BuildTimeDepsParentNode internal constructor(
    buildParams: BuildParams<*>,
    viewFactory: ((RibView) -> BuildTimeDepsParentView?)?,
    plugins: List<Plugin> = emptyList()
) : Node<BuildTimeDepsParentView>(
    buildParams = buildParams,
    viewFactory = viewFactory,
    plugins = plugins
), BuildTimeDepsParent
