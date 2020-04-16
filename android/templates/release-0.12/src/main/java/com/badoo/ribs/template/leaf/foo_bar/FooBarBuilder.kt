package com.badoo.ribs.template.leaf.foo_bar

import com.badoo.ribs.core.builder.BuildParams
import com.badoo.ribs.core.builder.SimpleBuilder
import com.badoo.ribs.customisation.customisationsBranchFor
import com.badoo.ribs.customisation.getOrDefault
import com.badoo.ribs.template.leaf.foo_bar.feature.FooBarFeature

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
        val feature = FooBarFeature()
        val interactor = createInteractor(buildParams, dependency, feature)

        return FooBarNode(
            buildParams = buildParams,
            viewFactory = customisation.viewFactory(null),
            input = dependency.fooBarInput(),
            output = dependency.fooBarOutput(),
            feature = feature,
            interactor = interactor
        )
    }

    private fun createInteractor(
        buildParams: BuildParams<*>,
        dependency: FooBar.Dependency,
        feature: FooBarFeature
    ) =
        FooBarInteractor(
            buildParams = buildParams,
            input = dependency.fooBarInput(),
            output = dependency.fooBarOutput(),
            feature = feature
        )
}
