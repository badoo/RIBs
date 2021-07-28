package com.badoo.ribs.samples.transitionanimations.rib.transitionAnimationsExample

import com.badoo.ribs.core.Node
import com.badoo.ribs.core.modality.BuildParams
import com.badoo.ribs.core.plugin.Plugin
import com.badoo.ribs.core.view.ViewFactory

class TransitionAnimationsExampleNode internal constructor(
    buildParams: BuildParams<*>,
    viewFactory: ViewFactory<TransitionAnimationsExampleView>?,
    plugins: List<Plugin> = emptyList()
) : Node<TransitionAnimationsExampleView>(
    buildParams = buildParams,
    viewFactory = viewFactory,
    plugins = plugins
), TransitionAnimationsExample
