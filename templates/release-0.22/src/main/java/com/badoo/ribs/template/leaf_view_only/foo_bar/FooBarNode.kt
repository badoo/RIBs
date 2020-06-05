package com.badoo.ribs.template.leaf_view_only.foo_bar

import android.view.ViewGroup
import com.badoo.ribs.clienthelper.Connectable
import com.badoo.ribs.clienthelper.NodeConnector
import com.badoo.ribs.core.Node
import com.badoo.ribs.core.builder.BuildParams
import com.badoo.ribs.core.plugin.Plugin
import com.badoo.ribs.template.leaf_view_only.foo_bar.FooBar.Input
import com.badoo.ribs.template.leaf_view_only.foo_bar.FooBar.Output

class FooBarNode internal constructor(
    buildParams: BuildParams<*>,
    viewFactory: ((ViewGroup) -> FooBarView?)?,
    plugins: List<Plugin> = emptyList(),
    connector: NodeConnector<Input, Output> = NodeConnector()
) : Node<FooBarView>(
    buildParams = buildParams,
    viewFactory = viewFactory,
    plugins = plugins
), FooBar, Connectable<Input, Output> by connector {

}
