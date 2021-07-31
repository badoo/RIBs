package com.badoo.ribs.samples.routing.back_stack.rib.back_stack_example

import com.badoo.ribs.core.Node
import com.badoo.ribs.core.modality.BuildParams
import com.badoo.ribs.core.plugin.Plugin
import com.badoo.ribs.core.view.ViewFactory

class BackStackExampleNode internal constructor(
    buildParams: BuildParams<*>,
    viewFactory: ViewFactory<BackStackExampleView>?,
    plugins: List<Plugin> = emptyList()
) : Node<BackStackExampleView>(
    buildParams = buildParams,
    viewFactory = viewFactory,
    plugins = plugins
), BackStackExample
