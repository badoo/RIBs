package com.badoo.ribs.samples.retained_instance_store.rib.counter

import com.badoo.ribs.core.Node
import com.badoo.ribs.core.modality.BuildParams
import com.badoo.ribs.core.plugin.Plugin
import com.badoo.ribs.core.view.RibView

internal class CounterNode(
        buildParams: BuildParams<*>,
        viewFactory: ((RibView) -> CounterView?)?,
        plugins: List<Plugin>
) : Node<CounterView>(
        buildParams = buildParams,
        viewFactory = viewFactory,
        plugins = plugins
), Counter