package com.badoo.ribs.samples.buildtime.rib.profile

import com.badoo.ribs.core.Node
import com.badoo.ribs.core.modality.BuildParams
import com.badoo.ribs.core.plugin.Plugin
import com.badoo.ribs.core.view.RibView

class ProfileNode internal constructor(
    buildParams: BuildParams<*>,
    viewFactory: ((RibView) -> ProfileView?)?,
    plugins: List<Plugin> = emptyList()
) : Node<ProfileView>(
    buildParams = buildParams,
    viewFactory = viewFactory,
    plugins = plugins
), Profile
