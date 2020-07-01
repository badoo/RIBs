package com.badoo.ribs.example.welcome

import com.badoo.ribs.builder.SimpleBuilder
import com.badoo.ribs.core.modality.BuildParams

class WelcomeBuilder(
    private val dependency: Welcome.Dependency
) : SimpleBuilder<Welcome>() {

    override fun build(buildParams: BuildParams<Nothing?>): Welcome {
        val customisation = buildParams.getOrDefault(Welcome.Customisation())
        val interactor = WelcomeInteractor(
            buildParams = buildParams,
            authDataSource = dependency.authDataSource
        )

        return WelcomeNode(
            buildParams = buildParams,
            viewFactory = customisation.viewFactory(null),
            plugins = listOf(interactor)
        )
    }

}
