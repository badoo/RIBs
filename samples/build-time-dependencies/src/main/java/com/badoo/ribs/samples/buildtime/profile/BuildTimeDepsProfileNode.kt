package com.badoo.ribs.samples.buildtime.profile

import com.badoo.ribs.core.Node
import com.badoo.ribs.core.modality.BuildParams
import com.badoo.ribs.core.plugin.Plugin
import com.badoo.ribs.core.view.RibView

class BuildTimeDepsProfileNode internal constructor(
    buildParams: BuildParams<*>,
    viewFactory: ((RibView) -> BuildTimeDepsProfileView?)?,
    plugins: List<Plugin> = emptyList()
) : Node<BuildTimeDepsProfileView>(
    buildParams = buildParams,
    viewFactory = viewFactory,
    plugins = plugins
), BuildTimeDepsProfile
