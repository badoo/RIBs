package com.badoo.ribs.example.component.app_bar

import androidx.lifecycle.Lifecycle
import com.badoo.mvicore.android.lifecycle.startStop
import com.badoo.mvicore.binder.using
import com.badoo.ribs.clienthelper.interactor.Interactor
import com.badoo.ribs.core.modality.BuildParams
import com.badoo.ribs.example.component.app_bar.analytics.AppBarAnalytics
import com.badoo.ribs.example.component.app_bar.mapper.ViewEventToAnalyticsEvent
import com.badoo.ribs.example.component.app_bar.mapper.ViewEventToOutput
import com.badoo.ribs.example.component.app_bar.routing.AppBarRouter.Configuration
import com.badoo.ribs.routing.source.backstack.BackStackFeature
import io.reactivex.functions.Consumer

internal class AppBarInteractor(
    buildParams: BuildParams<*>,
    private val output: Consumer<AppBar.Output>,
    private val backStack: BackStackFeature<Configuration>
) : Interactor<AppBar, AppBarView>(
    buildParams = buildParams
) {

    override fun onViewCreated(view: AppBarView, viewLifecycle: Lifecycle) {
        viewLifecycle.startStop {
            bind(view to output using ViewEventToOutput)
            bind(view to AppBarAnalytics using ViewEventToAnalyticsEvent)
        }
    }
}
