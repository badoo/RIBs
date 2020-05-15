package com.badoo.ribs.sandbox.rib.foo_bar

import android.view.ViewGroup
import com.badoo.ribs.core.Node
import com.badoo.ribs.core.builder.BuildParams
import com.badoo.ribs.core.plugin.Plugin

class FooBarNode(
    buildParams: BuildParams<*>,
    viewFactory: ((ViewGroup) -> FooBarView?)?,
    plugins: List<Plugin> = emptyList()
) : Node<FooBarView>(
    buildParams = buildParams,
    viewFactory = viewFactory,
    plugins = plugins
), FooBar {

}
