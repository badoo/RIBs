package com.badoo.ribs.sandbox.rib.compose_leaf

import com.badoo.ribs.core.builder.BuildParams
import com.badoo.ribs.core.builder.SimpleBuilder
import com.badoo.ribs.sandbox.rib.compose_leaf.feature.ComposeLeafFeature

class ComposeLeafBuilder(
    private val dependency: ComposeLeaf.Dependency
) : SimpleBuilder<ComposeLeaf>() {

    override fun build(buildParams: BuildParams<Nothing?>): ComposeLeaf {
        val customisation = buildParams.getOrDefault(ComposeLeaf.Customisation())
        val feature = feature()
        val interactor = interactor(buildParams, feature)

        return node(buildParams, customisation, interactor)
    }

    private fun feature() =
        ComposeLeafFeature()

    private fun interactor(
        buildParams: BuildParams<*>,
        feature: ComposeLeafFeature
    ) =
        ComposeLeafInteractor(
            buildParams = buildParams,
            feature = feature
        )

    private fun node(
        buildParams: BuildParams<Nothing?>,
        customisation: ComposeLeaf.Customisation,
        interactor: ComposeLeafInteractor
    ) =
        ComposeLeafNode(
            buildParams = buildParams,
            viewFactory = customisation.viewFactory(null),
            plugins = listOf(interactor)
        )
}
