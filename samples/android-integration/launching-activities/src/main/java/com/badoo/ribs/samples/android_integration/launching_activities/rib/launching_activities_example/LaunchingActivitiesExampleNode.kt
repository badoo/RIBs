package com.badoo.ribs.samples.android_integration.launching_activities.rib.launching_activities_example

import com.badoo.ribs.core.Node
import com.badoo.ribs.core.modality.BuildParams
import com.badoo.ribs.core.plugin.Plugin
import com.badoo.ribs.core.view.ViewFactory

internal class LaunchingActivitiesExampleNode(
    buildParams: BuildParams<*>,
    viewFactory: ViewFactory<LaunchingActivitiesExampleView>?,
    plugins: List<Plugin>
) : Node<LaunchingActivitiesExampleView>(
    buildParams = buildParams,
    viewFactory = viewFactory,
    plugins = plugins
), LaunchingActivitiesExample
