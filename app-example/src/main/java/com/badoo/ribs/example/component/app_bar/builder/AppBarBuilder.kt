package com.badoo.ribs.example.component.app_bar.builder

import com.badoo.ribs.builder.Builder
import com.badoo.ribs.core.modality.BuildParams
import com.badoo.ribs.example.component.app_bar.AppBar
import com.badoo.ribs.example.component.app_bar.builder.AppBarBuilder.Params

class AppBarBuilder(
    private val dependency: AppBar.Dependency
) : Builder<Params, AppBar>() {

    data class Params(
        val userId: String
    )

    override fun build(buildParams: BuildParams<Params>): AppBar =
        DaggerAppBarComponent
            .factory()
            .create(
                dependency = dependency,
                customisation = buildParams.getOrDefault(AppBar.Customisation()),
                buildParams = buildParams
            )
            .node()
}
