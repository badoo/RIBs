package com.badoo.ribs.example.component.app_bar.builder

import com.badoo.ribs.builder.SimpleBuilder
import com.badoo.ribs.core.modality.BuildParams
import com.badoo.ribs.example.component.app_bar.AppBar

class AppBarBuilder(
    private val dependency: AppBar.Dependency
) : SimpleBuilder<AppBar>() {

    override fun build(buildParams: BuildParams<Nothing?>): AppBar =
        DaggerAppBarComponent
            .factory()
            .create(
                dependency = dependency,
                customisation = buildParams.getOrDefault(AppBar.Customisation()),
                buildParams = buildParams
            )
            .node()
}
