package com.badoo.ribs.example.rib.foo_bar.builder

import com.badoo.ribs.core.BuildParams
import com.badoo.ribs.core.Builder
import com.badoo.ribs.core.Rib
import com.badoo.ribs.customisation.customisationsBranchFor
import com.badoo.ribs.customisation.getOrDefault
import com.badoo.ribs.example.rib.foo_bar.FooBar
import com.badoo.ribs.example.rib.foo_bar.FooBarNode

class FooBarBuilder(
    dependency: FooBar.Dependency
) : Builder<FooBar.Dependency, Nothing?, FooBarNode>() {

    override val dependency : FooBar.Dependency = object : FooBar.Dependency by dependency {
        override fun ribCustomisation() = dependency.customisationsBranchFor(FooBar::class)
    }

    override val rib: Rib =
        object : FooBar {}

    override fun build(buildParams: BuildParams<Nothing?>): FooBarNode =
        DaggerFooBarComponent
            .factory()
            .create(
                dependency = dependency,
                customisation = dependency.getOrDefault(FooBar.Customisation()),
                buildParams = buildParams
            )
            .node()
}
