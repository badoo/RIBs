package com.badoo.ribs.template.leaf.foo_bar.v1

import androidx.lifecycle.Lifecycle
import com.badoo.mvicore.android.lifecycle.createDestroy
import com.badoo.mvicore.android.lifecycle.startStop
import com.badoo.binder.using
import com.badoo.ribs.clienthelper.interactor.Interactor
import com.badoo.ribs.core.modality.BuildParams
import com.badoo.ribs.template.leaf.foo_bar.common.analytics.FooBarAnalytics
import com.badoo.ribs.template.leaf.foo_bar.common.feature.FooBarFeature
import com.badoo.ribs.template.leaf.foo_bar.common.mapper.InputToWish
import com.badoo.ribs.template.leaf.foo_bar.common.mapper.NewsToOutput
import com.badoo.ribs.template.leaf.foo_bar.common.mapper.StateToViewModel
import com.badoo.ribs.template.leaf.foo_bar.common.mapper.ViewEventToAnalyticsEvent
import com.badoo.ribs.template.leaf.foo_bar.common.mapper.ViewEventToWish
import com.badoo.ribs.template.leaf.foo_bar.common.view.FooBarView

internal class FooBarInteractor(
    buildParams: BuildParams<*>,
    private val feature: FooBarFeature
) : Interactor<FooBarRib, FooBarView>(
    buildParams = buildParams
) {

    override fun onCreate(nodeLifecycle: Lifecycle) {
        nodeLifecycle.createDestroy {
            bind(feature.news to rib.output using NewsToOutput)
            bind(rib.input to feature using InputToWish)
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
