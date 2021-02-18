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
        val interactor = LoginInteractor(
            buildParams = buildParams,
            feature = feature,
            activityStarter = dependency.activityStarter
        )

        return LoginNode(
            buildParams = buildParams,
            plugins = listOf(interactor)
        )
    }


}
