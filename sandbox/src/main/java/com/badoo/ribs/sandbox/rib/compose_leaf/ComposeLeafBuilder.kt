package com.badoo.ribs.sandbox.rib.compose_leaf

import com.badoo.ribs.builder.SimpleBuilder
import com.badoo.ribs.core.modality.BuildParams
import com.badoo.ribs.core.plugin.Plugin

class ComposeLeafBuilder(
    private val dependency: ComposeLeaf.Dependency
) : SimpleBuilder<ComposeLeaf>() {

    override fun build(buildParams: BuildParams<Nothing?>): ComposeLeaf {
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
        buildParams: BuildParams<Nothing?>,
        customisation: ComposeLeaf.Customisation,
        plugins: List<Plugin>
    ) =
        ComposeLeafNode(
            buildParams = buildParams,
            viewFactory = customisation.viewFactory(null),
            plugins = plugins
        )
}
