package com.badoo.ribs.samples.retained_instance_store.rib

import com.badoo.ribs.core.Node
import com.badoo.ribs.core.modality.BuildParams
import com.badoo.ribs.core.plugin.Plugin
import com.badoo.ribs.core.view.RibView

internal class RetainedInstanceNode(
        buildParams: BuildParams<*>,
        viewFactory: ((RibView) -> RetainedInstanceView?)?,
        plugins: List<Plugin>
) : Node<RetainedInstanceView>(
        buildParams = buildParams,
        viewFactory = viewFactory,
        plugins = plugins
), RetainedInstanceRib