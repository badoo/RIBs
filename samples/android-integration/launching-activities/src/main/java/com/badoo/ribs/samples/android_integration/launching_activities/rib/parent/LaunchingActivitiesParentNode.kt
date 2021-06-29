package com.badoo.ribs.samples.android_integration.launching_activities.rib.parent

import com.badoo.ribs.core.Node
import com.badoo.ribs.core.modality.BuildParams
import com.badoo.ribs.core.plugin.Plugin
import com.badoo.ribs.core.view.ViewFactory

internal class LaunchingActivitiesParentNode(
    buildParams: BuildParams<*>,
    viewFactory: ViewFactory<LaunchingActivitiesParentView>?,
    plugins: List<Plugin>
) : Node<LaunchingActivitiesParentView>(
    buildParams = buildParams,
    viewFactory = viewFactory,
    plugins = plugins
), LaunchingActivitiesParent
