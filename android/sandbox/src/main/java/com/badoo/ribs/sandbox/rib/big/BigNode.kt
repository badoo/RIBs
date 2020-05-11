package com.badoo.ribs.sandbox.rib.big

import android.view.ViewGroup
import com.badoo.ribs.core.Node
import com.badoo.ribs.core.builder.BuildParams
import com.badoo.ribs.core.plugin.Plugin

class BigNode(
    buildParams: BuildParams<*>,
    viewFactory: ((ViewGroup) -> BigView?)?,
    plugins: List<Plugin>
) : Node<BigView>(
    buildParams = buildParams,
    viewFactory = viewFactory,
    plugins = plugins
), Big
