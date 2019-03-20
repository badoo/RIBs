package com.badoo.ribs.example.rib.lorem_ipsum

import android.arch.lifecycle.Lifecycle
import android.os.Bundle
import com.badoo.mvicore.android.lifecycle.createDestroy
import com.badoo.mvicore.binder.using
import com.badoo.ribs.core.Interactor
import com.badoo.ribs.core.Router
import com.badoo.ribs.example.rib.lorem_ipsum.analytics.LoremIpsumAnalytics
import com.badoo.ribs.example.rib.lorem_ipsum.feature.LoremIpsumFeature
import com.badoo.ribs.example.rib.lorem_ipsum.mapper.InputToWish
import com.badoo.ribs.example.rib.lorem_ipsum.mapper.NewsToOutput
import com.badoo.ribs.example.rib.lorem_ipsum.mapper.ViewEventToAnalyticsEvent
import com.badoo.ribs.example.rib.lorem_ipsum.mapper.ViewEventToWish
import com.badoo.ribs.example.rib.lorem_ipsum.LoremIpsumRouter.Configuration
import io.reactivex.ObservableSource
import io.reactivex.functions.Consumer

class LoremIpsumInteractor(
    router: Router<Configuration, LoremIpsumView>,
//    private val input: ObservableSource<LoremIpsum.Input>,
//    private val output: Consumer<LoremIpsum.Output>,
    private val feature: LoremIpsumFeature
) : Interactor<Configuration, LoremIpsumView>(
    router = router,
    disposables = feature
) {

    override fun onAttach(ribLifecycle: Lifecycle, savedInstanceState: Bundle?) {
        ribLifecycle.createDestroy {
//            bind(feature.news to output using NewsToOutput)
//            bind(input to feature using InputToWish)
        }
    }

    override fun onViewCreated(view: LoremIpsumView, viewLifecycle: Lifecycle) {
        viewLifecycle.createDestroy {
            bind(view to LoremIpsumAnalytics using ViewEventToAnalyticsEvent)
            bind(view to feature using ViewEventToWish)
        }
    }
}
