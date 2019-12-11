package com.badoo.ribs.example.rib.main_hello_world.mapper

import com.badoo.ribs.example.rib.main_hello_world.MainHelloWorldView
import com.badoo.ribs.example.rib.main_hello_world.analytics.MainHelloWorldAnalytics
import com.badoo.ribs.example.rib.main_hello_world.analytics.MainHelloWorldAnalytics.Event.ViewEvent

internal object ViewEventToAnalyticsEvent : (MainHelloWorldView.Event) -> MainHelloWorldAnalytics.Event? {

    override fun invoke(event: MainHelloWorldView.Event): MainHelloWorldAnalytics.Event? =
        ViewEvent(event)
}
