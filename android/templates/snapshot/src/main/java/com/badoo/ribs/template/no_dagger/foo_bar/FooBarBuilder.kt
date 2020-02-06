package com.badoo.ribs.template.no_dagger.foo_bar

import android.os.Bundle
import com.badoo.ribs.core.builder.Builder
import com.badoo.ribs.customisation.customisationsBranchFor
import com.badoo.ribs.customisation.getOrDefault
import com.badoo.ribs.template.no_dagger.foo_bar.feature.FooBarFeature

class FooBarBuilder(
    dependency: FooBar.Dependency
) : Builder<FooBar.Dependency>() {

    override val dependency : FooBar.Dependency = object : FooBar.Dependency by dependency {
        override fun ribCustomisation() = dependency.customisationsBranchFor(FooBar::class)
    }

    fun build(buildParams: BuildParams<Nothing?>): FooBarNode {
        val customisation = dependency.getOrDefault(FooBar.Customisation())
        val feature = FooBarFeature()
        val router = FooBarRouter(savedInstanceState)
        val interactor = createInteractor(savedInstanceState, router, dependency, feature)

        return FooBarNode(
            savedInstanceState,
            customisation.viewFactory(null),
            router,
            dependency.fooBarInput(),
            dependency.fooBarOutput(),
            feature,
            interactor
        )
    }

    private fun createInteractor(
        savedInstanceState: Bundle?,
        router: FooBarRouter,
        dependency: FooBar.Dependency,
        feature: FooBarFeature
    ) =
        FooBarInteractor(
            savedInstanceState,
            router,
            dependency.fooBarInput(),
            dependency.fooBarOutput(),
            feature
        )
}
