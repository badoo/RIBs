package com.badoo.ribs.sandbox.rib.foo_bar

import android.view.ViewGroup
import com.badoo.ribs.core.Node
import com.badoo.ribs.core.builder.BuildParams
import com.badoo.ribs.core.plugin.Plugin

class FooBarNode(
    viewFactory: ((ViewGroup) -> FooBarView?)?,
    buildParams: BuildParams<*>,
    plugins: List<Plugin>
) : Node<FooBarView>(
    buildParams = buildParams,
    viewFactory = viewFactory,
    plugins = plugins
), FooBar {

}
