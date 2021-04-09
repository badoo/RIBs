package com.badoo.ribs.samples.android_integration.launching_activities.rib

import com.badoo.ribs.core.Node
import com.badoo.ribs.core.modality.BuildParams
import com.badoo.ribs.core.plugin.Plugin
import com.badoo.ribs.core.view.ViewFactory

internal class LaunchingActivitiesNode(
        buildParams: BuildParams<*>,
        viewFactory: ViewFactory<LaunchingActivitiesView>?,
        plugins: List<Plugin>
) : Node<LaunchingActivitiesView>(
        buildParams = buildParams,
        viewFactory = viewFactory,
        plugins = plugins
), LaunchingActivities
