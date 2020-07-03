package com.badoo.ribs.example.login

import com.badoo.ribs.builder.SimpleBuilder
import com.badoo.ribs.core.modality.BuildParams
import com.badoo.ribs.example.login.feature.LoginFeature

class LoginBuilder(
    private val dependency: Login.Dependency
) : SimpleBuilder<Login>() {

    override fun build(buildParams: BuildParams<Nothing?>): Login {
        val customisation = buildParams.getOrDefault(Login.Customisation())
        val feature = LoginFeature(dependency.authCodeDataSource, dependency.authDataSource)
        val interactor = interactor(buildParams, feature)

        return node(buildParams, customisation, interactor)
    }


    private fun interactor(
        buildParams: BuildParams<*>,
        feature: LoginFeature
    ) =
        LoginInteractor(
            buildParams = buildParams,
            feature = feature
        )

    private fun node(
        buildParams: BuildParams<Nothing?>,
        customisation: Login.Customisation,
        interactor: LoginInteractor
    ) =
        LoginNode(
            buildParams = buildParams,
            viewFactory = customisation.viewFactory(null),
            plugins = listOf(interactor)
        )
}
