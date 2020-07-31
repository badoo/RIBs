package com.badoo.ribs.sandbox.rib.big

import com.badoo.ribs.builder.SimpleBuilder
import com.badoo.ribs.core.modality.BuildParams
import com.badoo.ribs.sandbox.rib.big.routing.BigChildBuilders
import com.badoo.ribs.sandbox.rib.big.routing.BigRouter

class BigBuilder(
    dependency: Big.Dependency
) : SimpleBuilder<Big>() {

    private val builders by lazy { BigChildBuilders(dependency) }

    override fun build(buildParams: BuildParams<Nothing?>): Big {
        val customisation = buildParams.getOrDefault(Big.Customisation())
        val interactor = BigInteractor(
            buildParams = buildParams
        )
        val router = BigRouter(
            buildParams = buildParams,
            builders = builders
        )

        return BigNode(
            buildParams = buildParams,
            viewFactory = customisation.viewFactory(null),
            plugins =  listOf(
                interactor,
                router
            )
        )
    }
}
