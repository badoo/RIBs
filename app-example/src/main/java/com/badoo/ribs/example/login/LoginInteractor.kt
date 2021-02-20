package com.badoo.ribs.example.login

import android.net.Uri
import androidx.browser.customtabs.CustomTabsIntent
import androidx.lifecycle.Lifecycle
import com.badoo.ribs.android.activitystarter.ActivityStarter
import com.badoo.ribs.clienthelper.interactor.Interactor
import com.badoo.ribs.core.modality.BuildParams
import com.badoo.ribs.example.BuildConfig
import com.badoo.ribs.example.auth.AuthConfig
import com.badoo.ribs.example.login.feature.LoginFeature

internal class LoginInteractor(
    buildParams: BuildParams<*>,
    private val feature: LoginFeature,
    private val activityStarter: ActivityStarter
) : Interactor<Login, Nothing>(
    buildParams = buildParams
) {

    override fun onCreate(nodeLifecycle: Lifecycle) {
        launchAuthCustomTab()
    }

    private fun launchAuthCustomTab() {
        val intent = CustomTabsIntent.Builder()
            .build()
            .run {
                intent.setData(Uri.parse(AuthConfig.authUrl(BuildConfig.ACCESS_KEY)))
            }
        activityStarter.startActivity { intent }
    }
}
