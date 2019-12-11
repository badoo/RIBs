package com.badoo.ribs.example.rib.main_foo_bar.mapper

import com.badoo.ribs.example.rib.main_foo_bar.MainFooBarView
import com.badoo.ribs.example.rib.main_foo_bar.analytics.MainFooBarAnalytics
import com.badoo.ribs.example.rib.main_foo_bar.analytics.MainFooBarAnalytics.Event.ViewEvent

internal object ViewEventToAnalyticsEvent : (MainFooBarView.Event) -> MainFooBarAnalytics.Event? {

    override fun invoke(event: MainFooBarView.Event): MainFooBarAnalytics.Event? =
        ViewEvent(event)
}
