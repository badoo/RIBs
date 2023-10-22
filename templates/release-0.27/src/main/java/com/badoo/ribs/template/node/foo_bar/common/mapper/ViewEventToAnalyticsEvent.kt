package com.badoo.ribs.template.node.foo_bar.common.mapper

import com.badoo.ribs.template.node.foo_bar.common.view.FooBarView.Event
import com.badoo.ribs.template.node.foo_bar.common.analytics.FooBarAnalytics
import com.badoo.ribs.template.node.foo_bar.common.analytics.FooBarAnalytics.Event.ViewEvent

internal object ViewEventToAnalyticsEvent : (Event) -> FooBarAnalytics.Event? {

    override fun invoke(event: Event): FooBarAnalytics.Event? =
        ViewEvent(event)
}
