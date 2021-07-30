package com.badoo.ribs.samples.comms_nodes.rib.greeting_container

import com.badoo.ribs.core.Node
import com.badoo.ribs.core.modality.BuildParams
import com.badoo.ribs.core.plugin.Plugin

class GreetingContainerNode internal constructor(
    buildParams: BuildParams<*>,
    plugins: List<Plugin> = emptyList(),
) : Node<GreetingContainerView>(
    buildParams = buildParams,
    viewFactory = GreetingContainerViewImpl.Factory().invoke(null),
    plugins = plugins
), GreetingContainer
