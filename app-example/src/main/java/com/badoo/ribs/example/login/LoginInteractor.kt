package com.badoo.ribs.example.login

import android.net.Uri
import androidx.browser.customtabs.CustomTabsIntent
import androidx.lifecycle.Lifecycle
import com.badoo.mvicore.android.lifecycle.createDestroy
import com.badoo.mvicore.binder.using
import com.badoo.ribs.android.activitystarter.ActivityStarter
import com.badoo.ribs.clienthelper.interactor.Interactor
import com.badoo.ribs.core.modality.BuildParams
import com.badoo.ribs.example.BuildConfig
import com.badoo.ribs.example.auth.AuthConfig
import com.badoo.ribs.example.login.feature.LoginFeature
import com.badoo.ribs.example.login.mapper.InputToWish
import com.badoo.ribs.example.login.mapper.NewsToOutput

internal class LoginInteractor(
    buildParams: BuildParams<*>,
    private val feature: LoginFeature,
    private val activityStarter: ActivityStarter
) : Interactor<Login, Nothing>(
    buildParams = buildParams,
    disposables = feature
) {

    override fun onAttach(nodeLifecycle: Lifecycle) {
        nodeLifecycle.createDestroy {
            bind(feature.news to rib.output using NewsToOutput)
            bind(rib.input to feature using InputToWish)
        }

        val intent = CustomTabsIntent.Builder()
            .build()
        activityStarter.startActivity {
            intent.intent.setData(Uri.parse(AuthConfig.authUrl(BuildConfig.ACCESS_KEY)))
        }
    }
}
