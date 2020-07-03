package com.badoo.ribs.example.login.mapper

import com.badoo.ribs.example.login.LoginView.Event
import com.badoo.ribs.example.login.analytics.LoginAnalytics
import com.badoo.ribs.example.login.analytics.LoginAnalytics.Event.ViewEvent

internal object ViewEventToAnalyticsEvent : (Event) -> LoginAnalytics.Event? {

    override fun invoke(event: Event): LoginAnalytics.Event? =
        ViewEvent(event)
}
