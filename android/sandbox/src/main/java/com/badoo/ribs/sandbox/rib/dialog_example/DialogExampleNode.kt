package com.badoo.ribs.sandbox.rib.dialog_example

import android.view.ViewGroup
import com.badoo.ribs.core.Node
import com.badoo.ribs.core.builder.BuildParams
import com.badoo.ribs.core.plugin.Plugin

class DialogExampleNode(
    buildParams: BuildParams<*>,
    viewFactory: ((ViewGroup) -> DialogExampleView?)?,
    plugins: List<Plugin> = emptyList()
) : Node<DialogExampleView>(
    buildParams = buildParams,
    viewFactory = viewFactory,
    plugins = plugins
), DialogExample
