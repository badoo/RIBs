package com.badoo.ribs.samples.retained_instance_store.rib.retained_instance_example

import com.badoo.ribs.core.Node
import com.badoo.ribs.core.modality.BuildParams
import com.badoo.ribs.core.plugin.Plugin
import com.badoo.ribs.core.view.RibView

class RetainedInstanceExampleNode internal constructor(
    buildParams: BuildParams<*>,
    viewFactory: ((RibView) -> RetainedInstanceExampleView?)?,
    plugins: List<Plugin> = emptyList()
) : Node<RetainedInstanceExampleView>(
    buildParams = buildParams,
    viewFactory = viewFactory,
    plugins = plugins
), RetainedInstanceExample
