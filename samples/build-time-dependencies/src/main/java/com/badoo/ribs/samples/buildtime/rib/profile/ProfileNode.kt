package com.badoo.ribs.samples.buildtime.rib.profile

import com.badoo.ribs.core.Node
import com.badoo.ribs.core.modality.BuildParams
import com.badoo.ribs.core.plugin.Plugin
import com.badoo.ribs.core.view.ViewFactory

class ProfileNode internal constructor(
    buildParams: BuildParams<*>,
    viewFactory: ViewFactory<ProfileView>?,
    plugins: List<Plugin> = emptyList()
) : Node<ProfileView>(
    buildParams = buildParams,
    viewFactory = viewFactory,
    plugins = plugins
), Profile
