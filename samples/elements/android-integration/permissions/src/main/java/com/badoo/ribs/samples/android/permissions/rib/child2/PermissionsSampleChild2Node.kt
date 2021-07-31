package com.badoo.ribs.samples.android.permissions.rib.child2

import com.badoo.ribs.core.Node
import com.badoo.ribs.core.modality.BuildParams
import com.badoo.ribs.core.plugin.Plugin
import com.badoo.ribs.core.view.ViewFactory

class PermissionsSampleChild2Node(
    buildParams: BuildParams<Nothing?>,
    viewFactory: ViewFactory<PermissionsSampleChild2ViewImpl>,
    plugins: List<Plugin> = emptyList(),
) : Node<PermissionsSampleChild2ViewImpl>(
    buildParams = buildParams,
    viewFactory = viewFactory,
    plugins = plugins
), PermissionsSampleChild2
