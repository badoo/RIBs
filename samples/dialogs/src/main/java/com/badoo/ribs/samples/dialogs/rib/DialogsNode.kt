package com.badoo.ribs.samples.dialogs.rib

import com.badoo.ribs.core.Node
import com.badoo.ribs.core.modality.BuildParams
import com.badoo.ribs.core.plugin.Plugin
import com.badoo.ribs.core.view.RibView

internal class DialogsNode(
    buildParams: BuildParams<*>,
    viewFactory: ((RibView) -> DialogsView),
    plugins: List<Plugin>
) : Node<DialogsView>(
    buildParams = BuildParams.Empty(),
    viewFactory = viewFactory,
    plugins = plugins
), Dialogs
