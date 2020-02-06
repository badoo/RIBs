package com.badoo.ribs.template.rib_with_view.foo_bar

import android.os.Bundle
import androidx.lifecycle.Lifecycle
import com.badoo.ribs.core.BuildParams
import com.badoo.mvicore.android.lifecycle.createDestroy
import com.badoo.mvicore.android.lifecycle.startStop
import com.badoo.mvicore.binder.using
import com.badoo.ribs.core.Interactor
import com.badoo.ribs.core.Router
import com.badoo.ribs.template.rib_with_view.foo_bar.FooBarRouter.Configuration
import com.badoo.ribs.template.rib_with_view.foo_bar.FooBarRouter.Configuration.Content
import com.badoo.ribs.template.rib_with_view.foo_bar.FooBarRouter.Configuration.Overlay
import com.badoo.ribs.template.rib_with_view.foo_bar.analytics.FooBarAnalytics
import com.badoo.ribs.template.rib_with_view.foo_bar.feature.FooBarFeature
import com.badoo.ribs.template.rib_with_view.foo_bar.mapper.InputToWish
import com.badoo.ribs.template.rib_with_view.foo_bar.mapper.NewsToOutput
import com.badoo.ribs.template.rib_with_view.foo_bar.mapper.StateToViewModel
import com.badoo.ribs.template.rib_with_view.foo_bar.mapper.ViewEventToAnalyticsEvent
import com.badoo.ribs.template.rib_with_view.foo_bar.mapper.ViewEventToWish
import io.reactivex.ObservableSource
import io.reactivex.functions.Consumer

class FooBarInteractor(
    buildParams: BuildParams<Nothing?>,
    router: Router<Configuration, *, Content, Overlay, FooBarView>,
    private val input: ObservableSource<FooBar.Input>,
    private val output: Consumer<FooBar.Output>,
    private val feature: FooBarFeature
) : Interactor<Configuration, Content, Overlay, FooBarView>(
    buildParams = buildParams,
    router = router,
    disposables = feature
) {

    override fun onAttach(ribLifecycle: Lifecycle, savedInstanceState: Bundle?) {
        ribLifecycle.createDestroy {
            bind(feature.news to output using NewsToOutput)
            bind(input to feature using InputToWish)
        }
    }

    override fun onViewCreated(view: FooBarView, viewLifecycle: Lifecycle) {
        viewLifecycle.startStop {
            bind(feature to view using StateToViewModel)
            bind(view to feature using ViewEventToWish)
            bind(view to FooBarAnalytics using ViewEventToAnalyticsEvent)
        }
    }
}
