package com.badoo.ribs.example.rib.foo_bar

import com.badoo.ribs.core.builder.BuildParams
import com.badoo.ribs.core.builder.SimpleBuilder
import com.badoo.ribs.example.rib.foo_bar.viewplugin.ParentLongClickListener

class FooBarBuilder(
    private val dependency: FooBar.Dependency
) : SimpleBuilder<FooBar>() {

    override fun build(buildParams: BuildParams<Nothing?>): FooBar {
        val customisation = buildParams.getOrDefault(FooBar.Customisation())
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
