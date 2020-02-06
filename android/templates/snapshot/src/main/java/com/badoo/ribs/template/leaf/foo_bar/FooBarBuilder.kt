package com.badoo.ribs.template.leaf.foo_bar

import android.os.Bundle
import com.badoo.ribs.core.builder.Builder
import com.badoo.ribs.customisation.customisationsBranchFor
import com.badoo.ribs.customisation.getOrDefault
import com.badoo.ribs.template.leaf.foo_bar.feature.FooBarFeature

class FooBarBuilder(
    dependency: FooBar.Dependency
) : Builder<FooBar.Dependency>() {

    override val dependency : FooBar.Dependency = object : FooBar.Dependency by dependency {
        override fun ribCustomisation() = dependency.customisationsBranchFor(FooBar::class)
    }

    fun build(savedInstanceState: Bundle?): FooBarNode {
        val customisation = dependency.getOrDefault(FooBar.Customisation())
        val feature = FooBarFeature()
        val interactor = createInteractor(savedInstanceState, dependency, feature)

        return FooBarNode(
            savedInstanceState,
            customisation.viewFactory(null),
            dependency.fooBarInput(),
            dependency.fooBarOutput(),
            feature,
            interactor
        )
    }

    private fun createInteractor(
        savedInstanceState: Bundle?,
        dependency: FooBar.Dependency,
        feature: FooBarFeature
    ) =
        FooBarInteractor(
            savedInstanceState,
            dependency.fooBarInput(),
            dependency.fooBarOutput(),
            feature
        )
}
