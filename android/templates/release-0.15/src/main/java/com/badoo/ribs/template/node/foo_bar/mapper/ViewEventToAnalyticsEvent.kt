package com.badoo.ribs.template.node.foo_bar.mapper

import com.badoo.ribs.template.node.foo_bar.FooBarView.Event
import com.badoo.ribs.template.node.foo_bar.analytics.FooBarAnalytics
import com.badoo.ribs.template.node.foo_bar.analytics.FooBarAnalytics.Event.ViewEvent

internal object ViewEventToAnalyticsEvent : (Event) -> FooBarAnalytics.Event? {

    override fun invoke(event: Event): FooBarAnalytics.Event? =
        ViewEvent(event)
}
