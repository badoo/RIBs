package com.badoo.ribs.sandbox.rib.big

import com.badoo.ribs.core.Node
import com.badoo.ribs.core.modality.BuildParams
import com.badoo.ribs.core.plugin.Plugin
import com.badoo.ribs.core.view.ViewFactory

class BigNode(
    buildParams: BuildParams<*>,
    viewFactory: ViewFactory<BigView>?,
    plugins: List<Plugin>
) : Node<BigView>(
    buildParams = buildParams,
    viewFactory = viewFactory,
    plugins = plugins
), Big
