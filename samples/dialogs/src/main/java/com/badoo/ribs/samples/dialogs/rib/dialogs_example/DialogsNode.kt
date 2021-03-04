package com.badoo.ribs.samples.dialogs.rib.dialogs_example

import com.badoo.ribs.core.Node
import com.badoo.ribs.core.modality.BuildParams
import com.badoo.ribs.core.plugin.Plugin
import com.badoo.ribs.core.view.RibView

internal class DialogsNode(
    buildParams: BuildParams<*>,
    viewFactory: ((RibView) -> DialogsView),
    plugins: List<Plugin>
) : Node<DialogsView>(
    buildParams = buildParams,
    viewFactory = viewFactory,
    plugins = plugins
), DialogsExample
