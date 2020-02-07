package com.badoo.ribs.template.no_dagger.foo_bar

import com.badoo.ribs.core.builder.BuildParams
import com.badoo.ribs.core.builder.Builder
import com.badoo.ribs.customisation.customisationsBranchFor
import com.badoo.ribs.customisation.getOrDefault
import com.badoo.ribs.template.no_dagger.foo_bar.feature.FooBarFeature

class FooBarBuilder(
    dependency: FooBar.Dependency
) : Builder<FooBar.Dependency, FooBarNode>(
    rib = object : FooBar {}
) {

    override val dependency : FooBar.Dependency = object : FooBar.Dependency by dependency {
        override fun ribCustomisation() = dependency.customisationsBranchFor(FooBar::class)
    }

    override fun build(buildParams: BuildParams<Nothing?>): FooBarNode {
        val customisation = dependency.getOrDefault(FooBar.Customisation())
        val feature = FooBarFeature()
        val router = FooBarRouter(buildParams)
        val interactor = createInteractor(buildParams, router, dependency, feature)

        return FooBarNode(
            buildParams = buildParams,
            viewFactory = customisation.viewFactory(null),
            router = router,
            input = dependency.fooBarInput(),
            output = dependency.fooBarOutput(),
            feature = feature,
            interactor = interactor
        )
    }

    private fun createInteractor(
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
}
