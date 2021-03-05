package com.badoo.ribs.samples.retained_instance_store.rib.counter

import com.badoo.ribs.core.Node
import com.badoo.ribs.core.modality.BuildParams
import com.badoo.ribs.core.plugin.Plugin
import com.badoo.ribs.core.view.ViewFactory

internal class CounterNode(
    buildParams: BuildParams<*>,
    viewFactory: ViewFactory<CounterView>?,
    plugins: List<Plugin>
) : Node<CounterView>(
    buildParams = buildParams,
    viewFactory = viewFactory,
    plugins = plugins
), Counter