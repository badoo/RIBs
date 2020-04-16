package com.badoo.ribs.template.no_dagger.foo_bar

import com.badoo.ribs.core.builder.BuildParams
import com.badoo.ribs.core.builder.SimpleBuilder
import com.badoo.ribs.customisation.customisationsBranchFor
import com.badoo.ribs.customisation.getOrDefault
import com.badoo.ribs.template.no_dagger.foo_bar.feature.FooBarFeature

class FooBarBuilder(
    dependency: FooBar.Dependency
) : SimpleBuilder<FooBar.Dependency, FooBarNode>(
    rib = object : FooBar {}
) {

    override val dependency : FooBar.Dependency = object : FooBar.Dependency by dependency {
        override fun ribCustomisation() = dependency.customisationsBranchFor(FooBar::class)
    }

    override fun build(buildParams: BuildParams<Nothing?>): FooBarNode {
        val customisation = dependency.getOrDefault(FooBar.Customisation())
        val router = router(buildParams, customisation)
        val feature = FooBarFeature()
        val interactor = interactor(buildParams, router, dependency, feature)

        return node(buildParams, customisation, router, interactor)
    }

    private fun router(
        buildParams: BuildParams<Nothing?>,
        customisation: FooBar.Customisation
    ) =
        FooBarRouter(
            buildParams = buildParams,
            transitionHandler = customisation.transitionHandler
        )

    private fun interactor(
        buildParams: BuildParams<Nothing?>,
        router: FooBarRouter,
        dependency: FooBar.Dependency,
        feature: FooBarFeature
    ) =
        FooBarInteractor(
            buildParams = buildParams,
            router = router,
            input = dependency.fooBarInput(),
            output = dependency.fooBarOutput(),
            feature = feature
        )

    private fun node(
        buildParams: BuildParams<Nothing?>,
        customisation: FooBar.Customisation,
        router: FooBarRouter,
        interactor: FooBarInteractor
    ): FooBarNode {
        return FooBarNode(
            buildParams = buildParams,
            viewFactory = customisation.viewFactory(null),
            router = router,
            interactor = interactor
        )
    }
}
