package com.badoo.ribs.example.login

import android.app.Activity
import android.net.Uri
import androidx.browser.customtabs.CustomTabsIntent
import androidx.lifecycle.Lifecycle
import com.badoo.ribs.android.activitystarter.ActivityStarter
import com.badoo.ribs.android.subscribe
import com.badoo.ribs.clienthelper.interactor.Interactor
import com.badoo.ribs.core.modality.BuildParams
import com.badoo.ribs.example.BuildConfig
import com.badoo.ribs.example.auth.AuthConfig
import com.badoo.ribs.example.login.feature.LoginFeature
import com.badoo.ribs.minimal.reactive.CompositeCancellable

internal class LoginInteractor(
    buildParams: BuildParams<*>,
    private val feature: LoginFeature,
    private val activityStarter: ActivityStarter
) : Interactor<Login, Nothing>(
    buildParams = buildParams
) {
    private val cancellable = CompositeCancellable()
    override fun onCreate(nodeLifecycle: Lifecycle) {
        nodeLifecycle.subscribe(
            onCreate = {
                cancellable += activityStarter.events(this)
                    .observe {
                        handleResult(it)
                    }
            },
            onDestroy = { cancellable.cancel() }
        )
        launchAuthCustomTab()
    }

    private fun handleResult(result: ActivityStarter.ActivityResultEvent) {
        if (result.requestCode == REQUEST_CODE_LOGIN_CHROME_TAB && result.resultCode == Activity.RESULT_CANCELED) {
            node.upNavigation()
        }
    }

    private fun launchAuthCustomTab() {
        activityStarter.startActivityForResult(
            client = this,
            requestCode = REQUEST_CODE_LOGIN_CHROME_TAB
        ) {
            CustomTabsIntent.Builder()
                .build()
                .run {
                    intent.setData(Uri.parse(AuthConfig.authUrl(BuildConfig.ACCESS_KEY)))
                }
        }
    }

    companion object {
        private const val REQUEST_CODE_LOGIN_CHROME_TAB = 1
    }
}
