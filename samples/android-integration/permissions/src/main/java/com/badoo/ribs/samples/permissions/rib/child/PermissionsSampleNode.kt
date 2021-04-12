package com.badoo.ribs.samples.permissions.rib.child

import com.badoo.ribs.core.Node
import com.badoo.ribs.core.modality.BuildParams
import com.badoo.ribs.core.plugin.Plugin
import com.badoo.ribs.core.view.ViewFactory

class PermissionsSampleNode(
    buildParams: BuildParams<Nothing?>,
    viewFactory: ViewFactory<PermissionsView>,
    plugins: List<Plugin> = emptyList(),
) : Node<PermissionsView>(
    buildParams = buildParams,
    viewFactory = viewFactory,
    plugins = plugins
), PermissionsSample
