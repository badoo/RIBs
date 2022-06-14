package com.badoo.ribs.template.node.foo_bar.v1

import androidx.lifecycle.Lifecycle
import com.badoo.mvicore.android.lifecycle.createDestroy
import com.badoo.mvicore.android.lifecycle.startStop
import com.badoo.binder.using
import com.badoo.ribs.clienthelper.interactor.Interactor
import com.badoo.ribs.core.modality.BuildParams
import com.badoo.ribs.routing.source.backstack.BackStack
import com.badoo.ribs.template.node.foo_bar.common.analytics.FooBarAnalytics
import com.badoo.ribs.template.node.foo_bar.common.feature.FooBarFeature
import com.badoo.ribs.template.node.foo_bar.common.mapper.InputToWish
import com.badoo.ribs.template.node.foo_bar.common.mapper.NewsToOutput
import com.badoo.ribs.template.node.foo_bar.common.mapper.StateToViewModel
import com.badoo.ribs.template.node.foo_bar.common.mapper.ViewEventToAnalyticsEvent
import com.badoo.ribs.template.node.foo_bar.common.mapper.ViewEventToWish
import com.badoo.ribs.template.node.foo_bar.v1.routing.FooBarRouter.Configuration

internal class FooBarInteractor(
    buildParams: BuildParams<*>,
    private val backStack: BackStack<Configuration>,
    private val feature: FooBarFeature
) : Interactor<FooBarRib, FooBarView>(
    buildParams = buildParams
) {

    override fun onCreate(nodeLifecycle: Lifecycle) {
        nodeLifecycle.createDestroy {
            bind(feature.news to rib.output using NewsToOutput)
            bind(rib.input to feature using InputToWish)
        }
        
//        childAware(nodeLifecycle) {
//            createDestroy<Child1> { child1 ->
//                bind(child1.output to TODO())
//            }
//
//            createDestroy<Child1, Child2> { child1, child2 ->
//                bind(child1.output to child2.input)
//            }
//        }
    }

    override fun onViewCreated(view: FooBarView, viewLifecycle: Lifecycle) {
        viewLifecycle.startStop {
            bind(feature to view using StateToViewModel)
            bind(view to feature using ViewEventToWish)
            bind(view to FooBarAnalytics using ViewEventToAnalyticsEvent)
        }
    }
}
