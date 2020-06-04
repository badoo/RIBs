package com.badoo.ribs.sandbox.rib.blocker

import com.badoo.ribs.core.modality.BuildParams
import com.badoo.ribs.builder.SimpleBuilder


class BlockerBuilder(
    private val dependency: Blocker.Dependency
) : SimpleBuilder<Blocker>() {

    override fun build(buildParams: BuildParams<Nothing?>): Blocker {
        val customisation = buildParams.getOrDefault(Blocker.Customisation())
        val interactor = BlockerInteractor(
            buildParams = buildParams
        )

        return BlockerNode(
            buildParams = buildParams,
            viewFactory = customisation.viewFactory(null),
            plugins = listOf(
                interactor
            )
        )
    }
}
