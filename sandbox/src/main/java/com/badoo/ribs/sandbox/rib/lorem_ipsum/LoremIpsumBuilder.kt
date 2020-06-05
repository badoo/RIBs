package com.badoo.ribs.sandbox.rib.lorem_ipsum

import com.badoo.ribs.builder.SimpleBuilder
import com.badoo.ribs.core.modality.BuildParams

class LoremIpsumBuilder(
    private val dependency: LoremIpsum.Dependency
) : SimpleBuilder<LoremIpsum>() {

    override fun build(buildParams: BuildParams<Nothing?>): LoremIpsum {
        val customisation = buildParams.getOrDefault(LoremIpsum.Customisation())
        val interactor = LoremIpsumInteractor(
            buildParams = buildParams
        )

        return LoremIpsumNode(
            buildParams = buildParams,
            viewFactory = customisation.viewFactory(null),
            plugins = listOf(
                interactor
            )
        )
    }
}
