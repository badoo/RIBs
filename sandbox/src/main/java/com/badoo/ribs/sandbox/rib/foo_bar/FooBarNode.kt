package com.badoo.ribs.sandbox.rib.foo_bar

import com.badoo.ribs.core.Node
import com.badoo.ribs.core.modality.BuildParams
import com.badoo.ribs.core.plugin.Plugin
import com.badoo.ribs.core.view.ViewFactory
import com.badoo.ribs.rx3.clienthelper.connector.Connectable
import com.badoo.ribs.rx3.clienthelper.connector.NodeConnector
import com.badoo.ribs.sandbox.rib.foo_bar.FooBar.Input
import com.badoo.ribs.sandbox.rib.foo_bar.FooBar.Output

class FooBarNode(
    buildParams: BuildParams<*>,
    viewFactory: ViewFactory<FooBarView>?,
    plugins: List<Plugin> = emptyList(),
    connector: NodeConnector<Input, Output> = NodeConnector()
) : Node<FooBarView>(
    buildParams = buildParams,
    viewFactory = viewFactory,
    plugins = plugins
), FooBar, Connectable<Input, Output> by connector
