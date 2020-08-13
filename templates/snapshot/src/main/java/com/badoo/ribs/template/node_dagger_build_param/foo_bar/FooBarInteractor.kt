package com.badoo.ribs.template.node_dagger_build_param.foo_bar

import androidx.lifecycle.Lifecycle
import com.badoo.mvicore.android.lifecycle.createDestroy
import com.badoo.mvicore.android.lifecycle.startStop
import com.badoo.mvicore.binder.using
import com.badoo.ribs.clienthelper.interactor.BackStackInteractor
import com.badoo.ribs.core.modality.BuildParams
import com.badoo.ribs.routing.source.backstack.BackStackFeature
import com.badoo.ribs.template.node_dagger_build_param.foo_bar.analytics.FooBarAnalytics
import com.badoo.ribs.template.node_dagger_build_param.foo_bar.feature.FooBarFeature
import com.badoo.ribs.template.node_dagger_build_param.foo_bar.mapper.*
import com.badoo.ribs.template.node_dagger_build_param.foo_bar.routing.FooBarRouter.Configuration

internal class FooBarInteractor(
    buildParams: BuildParams<*>,
    backStack: BackStackFeature<Configuration>,
    private val feature: FooBarFeature
) : BackStackInteractor<FooBar, FooBarView, Configuration>(
    buildParams = buildParams,
    backStack = backStack
) {

    override fun onAttach(nodeLifecycle: Lifecycle) {
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
