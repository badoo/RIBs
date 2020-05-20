package com.badoo.ribs.sandbox.rib.menu

import android.view.ViewGroup
import com.badoo.ribs.clienthelper.Connectable
import com.badoo.ribs.clienthelper.NodeConnector
import com.badoo.ribs.core.Node
import com.badoo.ribs.core.builder.BuildParams
import com.badoo.ribs.core.plugin.Plugin
import com.badoo.ribs.sandbox.rib.menu.Menu.Input
import com.badoo.ribs.sandbox.rib.menu.Menu.Output

class MenuNode(
    buildParams: BuildParams<Nothing?>,
    viewFactory: (ViewGroup) -> MenuView,
    plugins: List<Plugin> = emptyList(),
    connector: NodeConnector<Input, Output> = NodeConnector()
): Node<MenuView>(
    buildParams = buildParams,
    viewFactory = viewFactory,
    plugins = plugins
), Menu, Connectable<Input, Output> by connector
