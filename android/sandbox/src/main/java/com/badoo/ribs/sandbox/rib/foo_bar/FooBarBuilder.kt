package com.badoo.ribs.sandbox.rib.foo_bar

import com.badoo.ribs.core.builder.BuildParams
import com.badoo.ribs.core.builder.SimpleBuilder
import com.badoo.ribs.sandbox.rib.foo_bar.viewplugin.ParentLongClickListener

class FooBarBuilder(
    private val dependency: FooBar.Dependency
) : SimpleBuilder<FooBar>() {

    override fun build(buildParams: BuildParams<Nothing?>): FooBar {
        val customisation = buildParams.getOrDefault(FooBar.Customisation())
        val interactor = FooBarInteractor(
            buildParams = buildParams,
            permissionRequester = dependency.permissionRequester()
        )

        return FooBarNode(
            buildParams = buildParams,
            viewFactory = customisation.viewFactory(null),
            plugins =  listOf(
                interactor,
                ParentLongClickListener())
            )
    }
}
