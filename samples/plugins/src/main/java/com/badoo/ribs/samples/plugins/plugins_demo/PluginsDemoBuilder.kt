package com.badoo.ribs.samples.plugins.plugins_demo

import com.badoo.ribs.builder.SimpleBuilder
import com.badoo.ribs.core.modality.BuildParams

class PluginsDemoBuilder(
    private val dependency: PluginsDemo.Dependency
) : SimpleBuilder<PluginsDemo>() {

    override fun build(buildParams: BuildParams<Nothing?>): PluginsDemo {
        val node = PluginsDemoNode(
            buildParams = buildParams,
            viewFactory = PluginsDemoViewImpl.Factory().invoke(null),
            plugins = emptyList()
        )

        return node
    }

}
