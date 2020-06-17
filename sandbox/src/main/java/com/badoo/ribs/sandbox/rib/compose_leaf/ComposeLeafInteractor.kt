package com.badoo.ribs.sandbox.rib.compose_leaf

import androidx.lifecycle.Lifecycle
import com.badoo.mvicore.android.lifecycle.createDestroy
import com.badoo.mvicore.android.lifecycle.startStop
import com.badoo.mvicore.binder.using
import com.badoo.ribs.clienthelper.interactor.Interactor
import com.badoo.ribs.core.modality.BuildParams
import com.badoo.ribs.sandbox.rib.compose_leaf.analytics.ComposeLeafAnalytics
import com.badoo.ribs.sandbox.rib.compose_leaf.feature.ComposeLeafFeature
import com.badoo.ribs.sandbox.rib.compose_leaf.mapper.InputToWish
import com.badoo.ribs.sandbox.rib.compose_leaf.mapper.NewsToOutput
import com.badoo.ribs.sandbox.rib.compose_leaf.mapper.StateToViewModel
import com.badoo.ribs.sandbox.rib.compose_leaf.mapper.ViewEventToAnalyticsEvent
import com.badoo.ribs.sandbox.rib.compose_leaf.mapper.ViewEventToWish

internal class ComposeLeafInteractor(
    buildParams: BuildParams<*>,
    private val feature: ComposeLeafFeature
) : Interactor<ComposeLeaf, ComposeLeafView>(
    buildParams = buildParams,
    disposables = feature
) {
    override fun onViewCreated(view: ComposeLeafView, viewLifecycle: Lifecycle) {
        viewLifecycle.startStop {
            bind(feature to view using StateToViewModel)
            bind(view to feature using ViewEventToWish)
            bind(view to ComposeLeafAnalytics using ViewEventToAnalyticsEvent)
        }
    }
}
