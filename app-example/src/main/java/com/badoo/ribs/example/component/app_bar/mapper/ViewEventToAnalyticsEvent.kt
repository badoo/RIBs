package com.badoo.ribs.example.component.app_bar.mapper

import com.badoo.ribs.example.component.app_bar.AppBarView.Event
import com.badoo.ribs.example.component.app_bar.analytics.AppBarAnalytics
import com.badoo.ribs.example.component.app_bar.analytics.AppBarAnalytics.Event.ViewEvent

internal object ViewEventToAnalyticsEvent : (Event) -> AppBarAnalytics.Event? {

    override fun invoke(event: Event): AppBarAnalytics.Event? =
        ViewEvent(event)
}
