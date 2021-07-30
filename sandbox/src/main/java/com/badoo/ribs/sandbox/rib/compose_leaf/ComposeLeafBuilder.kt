package com.badoo.ribs.sandbox.rib.compose_leaf

import com.badoo.ribs.builder.Builder
import com.badoo.ribs.core.modality.BuildParams
import com.badoo.ribs.core.plugin.Plugin

class ComposeLeafBuilder(
    private val dependency: ComposeLeaf.Dependency
) : Builder<ComposeLeaf.Params, ComposeLeaf>() {

    override fun build(buildParams: BuildParams<ComposeLeaf.Params>): ComposeLeaf {
        val customisation = buildParams.getOrDefault(ComposeLeaf.Customisation())
        val interactor = ComposeLeafInteractor(
            buildParams = buildParams
        )

        return node(
            buildParams = buildParams,
            customisation = customisation,
            plugins = listOf(interactor)
        )
    }

    private fun node(
        buildParams: BuildParams<*>,
        customisation: ComposeLeaf.Customisation,
        plugins: List<Plugin>
    ): ComposeLeaf =
        ComposeLeafNode(
            buildParams = buildParams,
            viewFactory = customisation.viewFactory(null),
            plugins = plugins
        )
}
