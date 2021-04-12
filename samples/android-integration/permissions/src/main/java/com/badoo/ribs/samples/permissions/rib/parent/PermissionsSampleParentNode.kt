package com.badoo.ribs.samples.permissions.rib.parent

import com.badoo.ribs.core.Node
import com.badoo.ribs.core.modality.BuildParams
import com.badoo.ribs.core.plugin.Plugin
import com.badoo.ribs.core.view.ViewFactory

class PermissionsSampleParentNode internal constructor(
    buildParams: BuildParams<*>,
    viewFactory: ViewFactory<PermissionsSampleParentView>,
    plugins: List<Plugin> = emptyList()
) : Node<PermissionsSampleParentView>(
    buildParams = buildParams,
    viewFactory = viewFactory,
    plugins = plugins
), PermissionsSampleParent
