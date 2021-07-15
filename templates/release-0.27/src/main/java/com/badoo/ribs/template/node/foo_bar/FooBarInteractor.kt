package com.badoo.ribs.template.node.foo_bar

import androidx.lifecycle.Lifecycle
import com.badoo.mvicore.android.lifecycle.createDestroy
import com.badoo.mvicore.android.lifecycle.startStop
import com.badoo.mvicore.binder.using
import com.badoo.ribs.clienthelper.interactor.Interactor
import com.badoo.ribs.core.modality.BuildParams
import com.badoo.ribs.routing.source.backstack.BackStack
import com.badoo.ribs.template.node.foo_bar.analytics.FooBarAnalytics
import com.badoo.ribs.template.node.foo_bar.feature.FooBarFeature
import com.badoo.ribs.template.node.foo_bar.mapper.InputToWish
import com.badoo.ribs.template.node.foo_bar.mapper.NewsToOutput
import com.badoo.ribs.template.node.foo_bar.mapper.StateToViewModel
import com.badoo.ribs.template.node.foo_bar.mapper.ViewEventToAnalyticsEvent
import com.badoo.ribs.template.node.foo_bar.mapper.ViewEventToWish
import com.badoo.ribs.template.node.foo_bar.routing.FooBarRouter.Configuration

internal class FooBarInteractor(
    buildParams: BuildParams<*>,
    private val backStack: BackStack<Configuration>,
    private val feature: FooBarFeature
) : Interactor<FooBar, FooBarView>(
    buildParams = buildParams
) {

    override fun onCreate(nodeLifecycle: Lifecycle) {
        nodeLifecycle.createDestroy {
            bind(feature.news to rib.output using NewsToOutput)
            bind(rib.input to feature using InputToWish)
        }

//        childAware(nodeLifecycle) {
//            whenChildAttached<Child1> { commonLifecycle, child1 ->
//                // TODO e.g. subscribe to child1.output
//                // TODO use commonLifecycle for scoping
//            }
//
//            whenChildrenAttached<Child1, Child2> { commonLifecycle, child1, child2 ->
//                // TODO connect e.g. child1.output to child2.input
//                // TODO use commonLifecycle for scoping
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
