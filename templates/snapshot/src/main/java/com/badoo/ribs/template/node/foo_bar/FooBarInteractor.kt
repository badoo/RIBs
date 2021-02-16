package com.badoo.ribs.template.node.foo_bar

import androidx.lifecycle.Lifecycle
import com.badoo.mvicore.android.lifecycle.createDestroy
import com.badoo.mvicore.android.lifecycle.startStop
import com.badoo.mvicore.binder.using
import com.badoo.ribs.clienthelper.interactor.Interactor
import com.badoo.ribs.core.Node
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
    }

    override fun onViewCreated(view: FooBarView, viewLifecycle: Lifecycle) {
        viewLifecycle.startStop {
            bind(feature to view using StateToViewModel)
            bind(view to feature using ViewEventToWish)
            bind(view to FooBarAnalytics using ViewEventToAnalyticsEvent)
        }
    }

    override fun onChildBuilt(child: Node<*>) {
        /**
         * TODO bind children here and delete this comment block.
         *
         *  At this point children haven't set their own bindings yet,
         *  so it's safe to setup listening to their output before they start emitting.
         *
         *  On the other hand, they're not ready to receive inputs yet. Usually this is alright.
         *  If it's a requirement though, create those bindings in [onChildAttached]
         */
        // child.lifecycle.createDestroy {
            // when (child) {
                // is Child1 -> bind(child.output to someConsumer)
            // }
        // }
    }
}
