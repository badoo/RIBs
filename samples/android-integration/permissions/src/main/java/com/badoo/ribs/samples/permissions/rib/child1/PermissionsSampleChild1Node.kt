package com.badoo.ribs.samples.permissions.rib.child1

import com.badoo.ribs.core.Node
import com.badoo.ribs.core.modality.BuildParams
import com.badoo.ribs.core.plugin.Plugin
import com.badoo.ribs.core.view.ViewFactory
import com.badoo.ribs.samples.permissions.rib.child2.PermissionsSampleChild2

class PermissionsSampleChild1Node(
    buildParams: BuildParams<Nothing?>,
    viewFactory: ViewFactory<PermissionsSampleChild1View>,
    plugins: List<Plugin> = emptyList(),
) : Node<PermissionsSampleChild1View>(
    buildParams = buildParams,
    viewFactory = viewFactory,
    plugins = plugins
), PermissionsSampleChild1
