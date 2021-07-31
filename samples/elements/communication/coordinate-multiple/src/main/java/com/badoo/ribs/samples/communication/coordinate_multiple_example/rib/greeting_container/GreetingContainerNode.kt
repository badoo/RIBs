package com.badoo.ribs.samples.communication.coordinate_multiple_example.rib.greeting_container

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
