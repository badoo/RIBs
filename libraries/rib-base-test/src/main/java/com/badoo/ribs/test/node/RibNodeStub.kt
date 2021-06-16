package com.badoo.ribs.test.node

import com.badoo.ribs.core.Node
import com.badoo.ribs.core.modality.BuildParams
import com.badoo.ribs.core.plugin.Plugin
import com.badoo.ribs.core.view.RibView
import com.badoo.ribs.core.view.ViewFactory
import com.badoo.ribs.test.emptyBuildParams

open class RibNodeStub<View : RibView>(
    buildParams: BuildParams<*> = emptyBuildParams(),
    plugins: List<Plugin> = emptyList(),
    viewFactory: ViewFactory<View>? = null,
) : Node<View>(
    buildParams = buildParams,
    viewFactory = viewFactory,
    plugins = plugins,
)
