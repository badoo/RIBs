package com.badoo.ribs.sandbox.rib.menu

import android.view.ViewGroup
import com.badoo.ribs.core.Node
import com.badoo.ribs.core.builder.BuildParams
import com.badoo.ribs.core.plugin.Plugin

class MenuNode(
    buildParams: BuildParams<Nothing?>,
    viewFactory: (ViewGroup) -> MenuView,
    plugins: List<Plugin> = emptyList()
): Node<MenuView>(
    buildParams = buildParams,
    viewFactory = viewFactory,
    plugins = plugins
), Menu
