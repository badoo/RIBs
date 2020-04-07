package com.badoo.ribs.example.rib.foo_bar

import com.badoo.ribs.core.builder.BuildParams
import com.badoo.ribs.core.builder.SimpleBuilder
import com.badoo.ribs.customisation.customisationsBranchFor
import com.badoo.ribs.customisation.getOrDefault
import com.badoo.ribs.example.rib.foo_bar.viewplugin.ParentLongClickListener

class FooBarBuilder(
    dependency: FooBar.Dependency
) : SimpleBuilder<FooBarNode>(
    rib = object : FooBar {}
) {

    private val dependency : FooBar.Dependency = object : FooBar.Dependency by dependency {
        override fun ribCustomisation() = dependency.customisationsBranchFor(FooBar::class)
    }

    override fun build(buildParams: BuildParams<Nothing?>): FooBarNode {
        val customisation = dependency.getOrDefault(FooBar.Customisation())
        val interactor = FooBarInteractor(
            buildParams = buildParams,
            permissionRequester = dependency.permissionRequester()
        )
        val viewPlugins = setOf(ParentLongClickListener())

        return FooBarNode(
            customisation.viewFactory(null),
            interactor = interactor,
            buildParams = buildParams,
            viewPlugins = viewPlugins
        )
    }
}
