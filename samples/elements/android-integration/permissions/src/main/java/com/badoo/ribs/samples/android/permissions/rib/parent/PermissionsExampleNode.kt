package com.badoo.ribs.samples.android.permissions.rib.parent

import com.badoo.ribs.core.Node
import com.badoo.ribs.core.modality.BuildParams
import com.badoo.ribs.core.plugin.Plugin
import com.badoo.ribs.core.view.ViewFactory

class PermissionsExampleNode internal constructor(
    buildParams: BuildParams<*>,
    viewFactory: ViewFactory<PermissionsExampleView>,
    plugins: List<Plugin> = emptyList()
) : Node<PermissionsExampleView>(
    buildParams = buildParams,
    viewFactory = viewFactory,
    plugins = plugins
), PermissionsExample
