package com.badoo.ribs.example.component.app_bar

import androidx.lifecycle.Lifecycle
import com.badoo.mvicore.android.lifecycle.createDestroy
import com.badoo.mvicore.android.lifecycle.startStop
import com.badoo.mvicore.binder.using
import com.badoo.ribs.clienthelper.interactor.Interactor
import com.badoo.ribs.core.modality.BuildParams
import com.badoo.ribs.example.component.app_bar.routing.AppBarRouter.Configuration
import com.badoo.ribs.example.component.app_bar.analytics.AppBarAnalytics
import com.badoo.ribs.example.component.app_bar.feature.AppBarFeature
import com.badoo.ribs.example.component.app_bar.mapper.InputToWish
import com.badoo.ribs.example.component.app_bar.mapper.NewsToOutput
import com.badoo.ribs.example.component.app_bar.mapper.StateToViewModel
import com.badoo.ribs.example.component.app_bar.mapper.ViewEventToAnalyticsEvent
import com.badoo.ribs.example.component.app_bar.mapper.ViewEventToWish
import com.badoo.ribs.routing.source.backstack.BackStackFeature

internal class AppBarInteractor(
    buildParams: BuildParams<*>,
    private val feature: AppBarFeature,
    private val backStack: BackStackFeature<Configuration>
) : Interactor<AppBar, AppBarView>(
    buildParams = buildParams,
    disposables = feature
) {

    override fun onAttach(nodeLifecycle: Lifecycle) {
        nodeLifecycle.createDestroy {
            bind(feature.news to rib.output using NewsToOutput)
            bind(rib.input to feature using InputToWish)
        }
    }

    override fun onViewCreated(view: AppBarView, viewLifecycle: Lifecycle) {
        viewLifecycle.startStop {
            bind(feature to view using StateToViewModel)
            bind(view to feature using ViewEventToWish)
            bind(view to AppBarAnalytics using ViewEventToAnalyticsEvent)
        }
    }
}
