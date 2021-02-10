package com.badoo.ribs.samples.retained_instance_store.rib.retainedInstance

import com.badoo.ribs.core.Node
import com.badoo.ribs.core.modality.BuildParams
import com.badoo.ribs.core.plugin.Plugin
import com.badoo.ribs.core.view.RibView

class RetainedInstanceNode internal constructor(
        buildParams: BuildParams<*>,
        viewFactory: ((RibView) -> RetainedInstanceView?)?,
        plugins: List<Plugin> = emptyList()
) : Node<RetainedInstanceView>(
    buildParams = buildParams,
    viewFactory = viewFactory,
    plugins = plugins
), RetainedInstance
