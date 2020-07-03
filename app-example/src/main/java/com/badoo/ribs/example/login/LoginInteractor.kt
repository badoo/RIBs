package com.badoo.ribs.example.login

import android.net.Uri
import androidx.browser.customtabs.CustomTabsIntent
import androidx.lifecycle.Lifecycle
import com.badoo.mvicore.android.lifecycle.createDestroy
import com.badoo.mvicore.android.lifecycle.startStop
import com.badoo.mvicore.binder.using
import com.badoo.ribs.clienthelper.interactor.Interactor
import com.badoo.ribs.core.modality.BuildParams
import com.badoo.ribs.example.BuildConfig
import com.badoo.ribs.example.auth.AuthConfig
import com.badoo.ribs.example.login.analytics.LoginAnalytics
import com.badoo.ribs.example.login.feature.LoginFeature
import com.badoo.ribs.example.login.mapper.InputToWish
import com.badoo.ribs.example.login.mapper.NewsToOutput
import com.badoo.ribs.example.login.mapper.StateToViewModel
import com.badoo.ribs.example.login.mapper.ViewEventToAnalyticsEvent
import com.badoo.ribs.example.login.mapper.ViewEventToWish

internal class LoginInteractor(
    buildParams: BuildParams<*>,
    private val feature: LoginFeature
) : Interactor<Login, LoginView>(
    buildParams = buildParams,
    disposables = feature
) {

    override fun onAttach(nodeLifecycle: Lifecycle) {
        nodeLifecycle.createDestroy {
            bind(feature.news to rib.output using NewsToOutput)
            bind(rib.input to feature using InputToWish)
        }
    }

    override fun onViewCreated(view: LoginView, viewLifecycle: Lifecycle) {
        val intent = CustomTabsIntent.Builder()
            .build()
        intent.launchUrl(
            view.androidView.context
            , Uri.parse(AuthConfig.authUrl(BuildConfig.ACCESS_KEY))
        )
        viewLifecycle.startStop {
            bind(feature to view using StateToViewModel)
            bind(view to feature using ViewEventToWish)
            bind(view to LoginAnalytics using ViewEventToAnalyticsEvent)
        }
    }
}
