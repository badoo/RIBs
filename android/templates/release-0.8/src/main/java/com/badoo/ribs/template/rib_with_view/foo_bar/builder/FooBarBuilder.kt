package com.badoo.ribs.template.rib_with_view.foo_bar.builder

import android.os.Bundle
import com.badoo.ribs.core.builder.Builder
import com.badoo.ribs.customisation.customisationsBranchFor
import com.badoo.ribs.customisation.getOrDefault
import com.badoo.ribs.template.rib_with_view.foo_bar.FooBar
import com.badoo.ribs.template.rib_with_view.foo_bar.FooBarNode

class FooBarBuilder(
    dependency: FooBar.Dependency
) : Builder<FooBar.Dependency>(object : FooBar {}) {

    override val dependency : FooBar.Dependency = object : FooBar.Dependency by dependency {
        override fun ribCustomisation() = dependency.customisationsBranchFor(FooBar::class)
    }

    fun build(buildParams: BuildParams<Nothing?>): FooBarNode =
        DaggerFooBarComponent
            .factory()
            .create(
                dependency = dependency,
                customisation = dependency.getOrDefault(FooBar.Customisation()),
                savedInstanceState = savedInstanceState
            )
            .node()
}
